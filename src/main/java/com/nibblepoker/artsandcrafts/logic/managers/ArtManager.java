package com.nibblepoker.artsandcrafts.logic.managers;

import com.mojang.logging.LogUtils;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.exceptions.InvalidArtDataException;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.EArtFormat;

import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The <i>ArtManager</i> is a caching solution that sits between the locally saved art pieces/presets and the
 * game/server logic.
 * It also centralises the art pieces' data in order to make moderation easier later down the line.
 *
 * @author Herwin Bozet (NibblePoker)
 */
public class ArtManager {
    private static final int DEFAULT_LOCK_TIMEOUT_MS = 100;

    private final ConcurrentHashMap<String, ArtData> artIndex;
    private final ConcurrentLinkedQueue<ArtData> unsavedArtData;
    private boolean isInitialized;
    private Thread internalThread = null;

    /**
     * Overly-strict lock used when either the game or internal thread needs to access some of the art's NBT data.
     * This should help prevent crashes when one side is saving the NBT data, and the other is querying/modifying it.
     * The client should wait at most 100ms and give up if the lock isn't released.
     * The internal thread should wait at minimum 200ms if the lock is used by the client.
     */
    private final ReentrantLock artNbtlock;

    public ArtManager() {
        this.isInitialized = false;
        this.artIndex = new ConcurrentHashMap<>();
        this.unsavedArtData = new ConcurrentLinkedQueue<>();
        this.artNbtlock = new ReentrantLock();
    }

    public void init(File artDataFolder) {
        this.internalThread = new Thread(new ArtManagerThread(this, artDataFolder));
        this.internalThread.setName("NibblePoker's ArtManager");
        this.internalThread.start();

        this.isInitialized = true;
    }

    public void deinit() {
        this.artIndex.clear();
        this.unsavedArtData.clear();

        // Stopping the thread.
        if(this.internalThread.isAlive()) {
            this.internalThread.interrupt();
        }
        // We now assume it is stopped.

        this.isInitialized = false;
    }

    public void queueArtDataForSaving(ArtData newArtData) {
        if(newArtData != null) {
            ArtsAndCraftsMod.LOGGER.debug("Queuing art '" + newArtData.getSha1String() + "' for saving...");
            this.unsavedArtData.add(newArtData);
        } else {
            ArtsAndCraftsMod.LOGGER.error("Failed to queue art data for saving, it was null !");
        }
    }

    public byte[] getImageData(String imageHash) {
        ArtData desiredArt = this.artIndex.get(imageHash);

        // If we have the data, we attempt to acquire the lock and extract the required info.
        if(desiredArt != null) {
            try {
                if(this.artNbtlock.tryLock(DEFAULT_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    // Making a complete copy to avoid getting a "ConcurrentModificationException".
                    byte[] imageByteData = desiredArt.getImageData();
                    byte[] copiedByteData = new byte[imageByteData.length];
                    System.arraycopy(imageByteData, 0, copiedByteData, 0, imageByteData.length);

                    // Lock gets freed in "finally" block.
                    return copiedByteData;
                }
            } catch (InterruptedException ignored) {
                // We don't really care about lock failures TBH.
            } finally {
                this.artNbtlock.unlock();
            }
        }

        // In the event we couldn't get the image data, we return null.
        return null;
    }

    /**
     * Retrieves the requested ArtData and returns a 1-to-1 copy of it for non-concurrent operations.
     * Mainly used to show images in the GUI since it's not realistically feasible to block the
     * sub-thread all the time.
     */
    public ArtData getArtDataCopy(String imageHash) {
        if(!isSHA1Hash(imageHash)) {
            return null;
        }

        ArtData desiredArt = this.artIndex.get(imageHash);

        // If we have the data, we attempt to acquire the lock and make a copy of it.
        if(desiredArt != null) {
            try {
                if(this.artNbtlock.tryLock(DEFAULT_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    // We have the lock and can finally make a copy.
                    return new ArtData(desiredArt.getNbtCopy());
                    // Lock gets freed in "finally" block.
                }
            } catch (InterruptedException ignored) {
                // We don't really care about lock failures TBH.
            } catch (InvalidArtDataException e) {
                ArtsAndCraftsMod.LOGGER.error(e.toString());
                return null;
            } finally {
                this.artNbtlock.unlock();
            }

            // In the event we couldn't get the lock, we indicate that it's loading and should be retried later.
            return null;
        }

        // We don't have that image cached.
        return null;
    }

    public boolean hasImage(String imageHash) {
        return this.artIndex.containsKey(imageHash);
    }

    public EArtFormat getImageFormat(String imageHash) {
        ArtData desiredArt = this.artIndex.get(imageHash);

        // If we have the data, we attempt to acquire the lock and extract the required info.
        if(desiredArt != null) {
            try {
                if(this.artNbtlock.tryLock(DEFAULT_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    // Lock gets freed in "finally" block.
                    return EArtFormat.getFromCode(desiredArt.getImageFormat());
                }
            } catch (InterruptedException ignored) {
                // We don't really care about lock failures TBH.
            } finally {
                this.artNbtlock.unlock();
            }

            // In the event we couldn't get the lock, we indicate that it's loading and should be retried later.
            return EArtFormat.LOADING;
        }

        // We don't have that image cached.
        return EArtFormat.UNKNOWN_UNCACHED;
    }

    public boolean isRunning() {
        return this.isInitialized && this.internalThread != null && this.internalThread.isAlive();
    }

    private static class ArtManagerThread implements Runnable {
        private final Logger logger = LogUtils.getLogger();
        private final ArtManager artManager;
        private final File artDataFolder;

        public ArtManagerThread(ArtManager artManager, File artDataFolder) {
            this.artManager = artManager;

            // We're making a "proper" clone of this Object.
            this.artDataFolder = new File(artDataFolder.getAbsolutePath());
        }

        public void run() {
            this.logger.info("The 'ArtManagerThread' Runnable has started !");

            // Getting a list of all cached ".art" files...
            this.artManager.artNbtlock.lock();
            this.loadCachedArt();
            this.artManager.artNbtlock.unlock();

            while(true) {
                if(!this.artManager.unsavedArtData.isEmpty()) {
                    // Saving an art piece that was in the list.
                    ArtData artDataToSave = this.artManager.unsavedArtData.poll();
                    try {
                        if(this.artManager.artNbtlock.tryLock(DEFAULT_LOCK_TIMEOUT_MS * 5, TimeUnit.MILLISECONDS)) {
                            // Lock gets freed in "finally" block.
                            artDataToSave.save();

                            // Once saved, we add it to the index.
                            this.artManager.artNbtlock.lock();
                            this.artManager.artIndex.put(artDataToSave.getSha1String(), artDataToSave);
                            this.artManager.artNbtlock.unlock();
                        } else {
                            // We couldn't get the lock, we requeue the art for saving.
                            this.artManager.unsavedArtData.add(artDataToSave);
                        }
                    } catch (InterruptedException ignored) {
                        // We don't care about other failures.
                    } finally {
                        this.artManager.artNbtlock.unlock();
                    }
                } else {
                    // Waiting for a bit to prevent CPU & lock hogging.
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ignored) {}
                }
            }
        }

        /**
         * Lists and loads all cached ".art.nbt" files present in "NibblePokerData/np_arts_and_crafts/".
         */
        private void loadCachedArt() {
            int artLoadCount = 0;

            this.logger.info("Queuing cached '.art.nbt' files...");
            for(String item : Objects.requireNonNull(this.artDataFolder.list((dir, name) -> name.endsWith(".art.nbt")))) {
                this.logger.debug("Loading '" + item + "'...");
                try {
                    ArtData artItem = ArtData.fromFile(new File(this.artDataFolder, item));
                    if(artItem != null) {
                        this.artManager.artIndex.put(artItem.getSha1String(), artItem);
                        artLoadCount++;
                    }
                } catch (Exception ignored) {}
            }
            this.logger.info("Loaded '" + artLoadCount + "' piece(s) of art !");
            this.logger.info("Cached '" + this.artManager.artIndex.size() + "' unique piece(s) of art !");
        }
    }

    /**
     * DO NOT USE THIS !!!
     */
    public void doDebugPrintout() {
        System.out.println("List of known art keys:");
        for(String artKey : this.artIndex.keySet()) {
            System.out.println("> " + artKey);
        }
    }

    public static boolean isSHA1Hash(String input) {
        return input != null && input.matches("[0-9a-fA-F]{40}");
    }
}
