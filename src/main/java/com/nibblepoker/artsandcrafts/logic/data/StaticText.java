package com.nibblepoker.artsandcrafts.logic.data;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.network.chat.Component;

public class StaticText {
        /** Server received "ArtData" it couldn't load. */
        public static final Component ERROR_BAD_DATA_TEXT = Component.translatable(
                "text." + ArtsAndCraftsMod.MOD_ID + ".packet.server_received_invalid_art");

        /** Server received "ArtData" that it already had in its "ArtManager". */
        public static final Component ERROR_DUPLICATE_TEXT = Component.translatable(
                "text." + ArtsAndCraftsMod.MOD_ID + ".packet.server_has_duplicate");

        /** Server received and queued art for saving and serving. */
        public static final Component PROCESSING_OK_TEXT = Component.translatable(
                "text." + ArtsAndCraftsMod.MOD_ID + ".packet.server_processing_art");

        /** Server received a report for an image it doesn't have in its index. */
        public static final Component REPORT_NOT_KNOWN_TEXT = Component.translatable(
                "text." + ArtsAndCraftsMod.MOD_ID + ".packet.server_unknown_art_report");

        /** Server received and processed a valid report. */
        public static final Component REPORT_OK_TEXT = Component.translatable(
                "text." + ArtsAndCraftsMod.MOD_ID + ".packet.server_reported_art");
}
