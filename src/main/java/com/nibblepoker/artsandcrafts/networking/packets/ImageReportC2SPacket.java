package com.nibblepoker.artsandcrafts.networking.packets;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.logic.data.StaticText;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ImageReportC2SPacket {
    private final String artHash;

    private final boolean notifyOnError, notifyOnSuccess;

    public ImageReportC2SPacket(String artHash, boolean notifyOnError, boolean notifyOnSuccess) {
        // We prepare the packet on the sender's side.
        this.artHash = artHash;
        this.notifyOnError = notifyOnError;
        this.notifyOnSuccess = notifyOnSuccess;
    }

    public ImageReportC2SPacket(FriendlyByteBuf buf) {
        // We prepare the packet on the recipient's side.
        this.artHash = buf.readUtf();
        this.notifyOnError = buf.readBoolean();
        this.notifyOnSuccess = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.artHash);
        buf.writeBoolean(this.notifyOnError);
        buf.writeBoolean(this.notifyOnSuccess);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            // Shouldn't happen.
            if(player == null) {
                ArtsAndCraftsMod.LOGGER.error("Discarding art report coming from 'null' player !");
                return;
            }

            // Checking if the server knows the hash.
            if(!ArtsAndCraftsMod.artManager.hasImage(this.artHash)) {
                ArtsAndCraftsMod.LOGGER.error("Received report for unknown art '"+this.artHash+
                        "' from '"+player.getName().getString()+"' !");
                if(this.notifyOnError) {
                    player.sendSystemMessage(StaticText.REPORT_NOT_KNOWN_TEXT, false);
                }
                return;
            }

            // TODO: Keep the report in a list
            if(this.notifyOnSuccess) {
                player.sendSystemMessage(StaticText.REPORT_OK_TEXT, false);
            }
        });

        return true;
    }
}
