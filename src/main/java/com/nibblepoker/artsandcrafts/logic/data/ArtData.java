package com.nibblepoker.artsandcrafts.logic.data;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;

import com.nibblepoker.artsandcrafts.exceptions.InvalidArtDataException;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.nbt.CompoundTag;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ArtData {
    private static final int MAGIC_NUMBER = 0x4E508EDF;
    private static final short VERSION_NUMBER = 0x0001;

    private final CompoundTag artNbtData;

    public ArtData(CompoundTag artNbtData) throws InvalidArtDataException {
        this.artNbtData = artNbtData;

        // Temporary variables
        boolean wasUpgraded = false;

        // Checking the magic number
        if(!this.artNbtData.contains("magic", CompoundTag.TAG_INT)) {
            throw new InvalidArtDataException("Unable to find the 'magic' int field !");
        }
        if(this.artNbtData.getInt("magic") != ArtData.MAGIC_NUMBER) {
            throw new InvalidArtDataException("Invalid 'magic' value !");
        }

        // Checking the version number
        if(this.artNbtData.getShort("version") > ArtData.VERSION_NUMBER) {
            throw new InvalidArtDataException("Version is newer than expected !");
        }
        // TODO: If older version is found, update it here.

        // Checking and preparing all other fields
        if(!this.artNbtData.contains("banned", CompoundTag.TAG_BYTE)) {
            throw new InvalidArtDataException("Unable to find the 'banned' byte field !");
        }
        if(!this.artNbtData.contains("author", CompoundTag.TAG_INT_ARRAY) ||
                this.artNbtData.getIntArray("author").length != 4) {
            this.artNbtData.remove("author");
            this.artNbtData.putUUID("author", new UUID(0L, 0L));
            wasUpgraded = true;
        }
        if(!this.artNbtData.contains("art", CompoundTag.TAG_BYTE_ARRAY)) {
            throw new InvalidArtDataException("Unable to find the 'art' byte array field !");
        }
        if(!this.artNbtData.contains("format", CompoundTag.TAG_INT)) {
            throw new InvalidArtDataException("Unable to find the 'format' int field !");
        }

        // Checking if we need to update the file itself.
        if(wasUpgraded) {
            this.save();
        }

        System.out.println(this.artNbtData.getString("name"));
    }

    public byte[] getImageData() {
        return this.artNbtData.getByteArray("art");
    }

    public int getImageFormat() {
        return this.artNbtData.getInt("format");
    }

    public UUID getAuthorUUID() {
        try {
            return this.artNbtData.getUUID("author");
        } catch(Exception e) {
            return new UUID(0L, 0L);
        }
    }

    public void setAuthorUUID(UUID authorUUID) {
        this.artNbtData.remove("author");
        this.artNbtData.putUUID("author", authorUUID);
    }

    public byte[] getSha1() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(this.artNbtData.getByteArray("art"));
            return digest.digest();
        } catch(NoSuchAlgorithmException ignored) {
            ArtsAndCraftsMod.LOGGER.error("Unable to get a 'SHA-1' MessageDigest !");
            return new byte[] {0x00};
        }
    }

    public String getSha1String() {
        return String.format("%040x", new BigInteger(1, this.getSha1()));
    }

    public boolean save() {
        File outputFile = new File(ArtsAndCraftsMod.dataFolderPath, this.getSha1String().toLowerCase() + ".art.nbt");

        if(outputFile.exists() && outputFile.isFile()) {
            if(!outputFile.delete()) {
                return false;
            }
        }

        try {
            NbtIo.write(this.artNbtData, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArtData fromFile(File artFile) {
        try {
            return new ArtData(NbtIo.read(artFile));
        } catch(Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
