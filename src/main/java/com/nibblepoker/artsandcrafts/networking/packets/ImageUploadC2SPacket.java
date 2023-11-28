package com.nibblepoker.artsandcrafts.networking.packets;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.exceptions.InvalidArtDataException;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.StaticText;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet used to transfer the entire NBT data contained in "ArtData" from a client to the server.
 */
public class ImageUploadC2SPacket {

    private final ArtData artData;

    private final boolean notifyOnError, notifyOnSuccess;

    public ImageUploadC2SPacket(ArtData sentArtData, boolean notifyOnError, boolean notifyOnSuccess) {
        // We prepare the packet on the sender's side.
        this.artData = sentArtData;
        this.notifyOnError = notifyOnError;
        this.notifyOnSuccess = notifyOnSuccess;
    }

    public ImageUploadC2SPacket(FriendlyByteBuf buf) {
        // We prepare the packet on the recipient's side.
        ArtData receivedArtData;
        try {
            receivedArtData = new ArtData(buf.readNbt());
        } catch (InvalidArtDataException ignored) {
            receivedArtData = null;
        }
        this.artData = receivedArtData;

        this.notifyOnError = buf.readBoolean();
        this.notifyOnSuccess = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(artData.getNbt());
        buf.writeBoolean(this.notifyOnError);
        buf.writeBoolean(this.notifyOnSuccess);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            // Shouldn't happen.
            if(player == null) {
                ArtsAndCraftsMod.LOGGER.error("Discarding art data coming from 'null' player !");
                return;
            }

            // The art data was corrupted/invalid in some way.
            if(this.artData == null) {
                ArtsAndCraftsMod.LOGGER.error("Received invalid art data from '"+player.getName().getString()+"' !");
                if(this.notifyOnError) {
                    player.sendSystemMessage(StaticText.ERROR_BAD_DATA_TEXT, false);
                }
                return;
            }

            ArtsAndCraftsMod.LOGGER.debug("Received art '" + this.artData.getSha1String() +
                    "' from '" + player.getName().getString() + "'");

            if(ArtsAndCraftsMod.artManager.hasImage(this.artData.getSha1String())) {
                ArtsAndCraftsMod.LOGGER.warn("Received duplicate art '" + this.artData.getSha1String() +
                        "' from '" + player.getName().getString() + "' !");
                if(this.notifyOnError) {
                    player.sendSystemMessage(StaticText.ERROR_DUPLICATE_TEXT, false);
                }
                return;
            }

            // We now have a piece of art that isn't cached by the server.

            // Setting up the author field.
            this.artData.setAuthorUUID(player.getUUID());

            // The server accepted the art.
            ArtsAndCraftsMod.artManager.queueArtDataForSaving(this.artData);
            if(this.notifyOnSuccess) {
                player.sendSystemMessage(StaticText.PROCESSING_OK_TEXT, false);
            }
        });

        return true;
    }
}
