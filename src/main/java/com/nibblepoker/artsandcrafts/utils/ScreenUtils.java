package com.nibblepoker.artsandcrafts.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenUtils {
    public static final int COLOR_TRANSPARENT = FastColor.ARGB32.color(0x00, 0x00, 0x00, 0x00);
    public static final int COLOR_TITLE = 0x3F3F3F;
    public static final int COLOR_WHITE = 0xFFFFFFFF;
    public static final int COLOR_TEXT_BUTTON = COLOR_WHITE;
    public static final int COLOR_TEXT_BUTTON_HOVER = FastColor.ARGB32.color(0xFF, 0xFF, 0xFF, 0xA0);
    public static final int COLOR_TEXT_BUTTON_DISABLED = 0xA0A0A0;
    public static final int COLOR_TEXT_INPUT = FastColor.ARGB32.color(0xFF, 0x3F, 0x3F, 0x3F);
    public static final int COLOR_TEXT_INPUT_DARK = FastColor.ARGB32.color(0xFF, 0x17, 0x17, 0x17);

    public static int swapRGB(int argbColor) {
        // I hate both Mojang and OpenGL equally for this.
        // Why, just why ?!?
        return FastColor.ARGB32.color(
                FastColor.ABGR32.alpha(argbColor),
                FastColor.ABGR32.red(argbColor),
                FastColor.ABGR32.green(argbColor),
                FastColor.ABGR32.blue(argbColor)
        );
    }

    public static void drawUnshadedCenteredString(GuiGraphics graphics, Font font, Component component, int centerX, int centerY, int color) {
        drawUnshadedCenteredString(graphics, font, component.getString(), centerX, centerY, color);
    }

    public static void drawUnshadedCenteredString(GuiGraphics graphics, Font font, String text, int centerX, int centerY, int color) {
        graphics.drawString(font, text,
                centerX - (font.width(text) / 2), centerY - (font.lineHeight / 2),
                color, false);
    }

    public static void drawShadedCenteredString(GuiGraphics graphics, Font font, Component component, int centerX, int centerY, int color) {
        drawUnshadedCenteredString(graphics, font, component.getString(), centerX, centerY, color);
    }

    public static void drawShadedCenteredString(GuiGraphics graphics, Font font, String text, int centerX, int centerY, int color) {
        graphics.drawString(font, text,
                centerX - (font.width(text) / 2), centerY - (font.lineHeight / 2),
                color, true);
    }
}
