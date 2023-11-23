package com.nibblepoker.artsandcrafts.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenUtils {
    public static final int COLOR_TITLE = 0x3F3F3F;

    public static void drawUnshadedCenteredString(GuiGraphics graphics, Font font, Component component, int centerX, int centerY, int color) {
        drawUnshadedCenteredString(graphics, font, component.getString(), centerX, centerY, color);
    }

    public static void drawUnshadedCenteredString(GuiGraphics graphics, Font font, String text, int centerX, int centerY, int color) {
        graphics.drawString(font, text,
                centerX - (font.width(text) / 2), centerY - (font.lineHeight / 2),
                color, false);
    }
}
