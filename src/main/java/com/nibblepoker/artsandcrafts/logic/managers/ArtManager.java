package com.nibblepoker.artsandcrafts.logic.managers;

import com.mojang.logging.LogUtils;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import org.slf4j.Logger;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The <i>ArtManager</i> is a caching solution that sits between the locally saved art pieces/presets and the
 * game/server logic.
 * It also centralises the art pieces' data in order to make moderation easier later down the line.
 *
 * @author Herwin Bozet (NibblePoker)
 */
public class ArtManager {
    private final ConcurrentHashMap<String, ArtData> artIndex;
    private final ConcurrentLinkedQueue<ArtData> unsavedArtData;
    private boolean isInitialized;
    private Thread internalThread = null;

    public ArtManager() {
        this.isInitialized = false;
        this.artIndex = new ConcurrentHashMap<>();
        this.unsavedArtData = new ConcurrentLinkedQueue<>();
    }

    public void init(File artDataFolder) {
        this.internalThread = new Thread(new ArtManagerThread(this, artDataFolder));
        internalThread.setName("NibblePoker's ArtManager");
        internalThread.start();

        this.isInitialized = true;
    }

    public void deinit() {
        this.artIndex.clear();
        this.unsavedArtData.clear();

        this.isInitialized = false;
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
            this.loadCachedArt();

            while(true) {
                if(!this.artManager.unsavedArtData.isEmpty()) {
                    // Saving an art piece that was in the list.
                    ArtData artDataToSave = this.artManager.unsavedArtData.poll();
                    artDataToSave.save();
                } else {
                    // Waiting for a bit to prevent CPU hogging.
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
            this.logger.info("Queuing cached '.art.nbt' files...");
            for (String item : Objects.requireNonNull(this.artDataFolder.list((dir, name) -> name.endsWith(".art.nbt")))) {
                this.logger.debug("Loading '" + item + "'...");
                try {
                    ArtData artItem = ArtData.fromFile(new File(this.artDataFolder, item));
                    if(artItem != null) {
                        this.artManager.artIndex.put(artItem.getSha1String(), artItem);
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
