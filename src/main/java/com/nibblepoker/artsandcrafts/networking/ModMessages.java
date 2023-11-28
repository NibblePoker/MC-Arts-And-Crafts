package com.nibblepoker.artsandcrafts.networking;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.networking.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel msgChannel;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        ArtsAndCraftsMod.LOGGER.info("Registering mod packets...");

        // Preparing the channel with generic config
        msgChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ArtsAndCraftsMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        // Registering packets

        // Packets > Image Data Transfer
        msgChannel.messageBuilder(ImageUploadC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ImageUploadC2SPacket::new)
                .encoder(ImageUploadC2SPacket::toBytes)
                .consumerMainThread(ImageUploadC2SPacket::handle)
                .add();

        // Packets > Image Report
        msgChannel.messageBuilder(ImageReportC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ImageReportC2SPacket::new)
                .encoder(ImageReportC2SPacket::toBytes)
                .consumerMainThread(ImageReportC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        msgChannel.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        msgChannel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        msgChannel.send(PacketDistributor.ALL.noArg(), message);
    }
}
