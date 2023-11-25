package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.SliderGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ColorEditorSideScreen extends NPScreen {
    private final static Component TEXT_RGB = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.rgb");
    private final static Component TEXT_R = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.r");
    private final static Component TEXT_G = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.g");
    private final static Component TEXT_B = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.b");
    private final static Component TEXT_ALPHA = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.alpha");
    private final static Component TEXT_A = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.a");
    private final static Component TEXT_HEX = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.hex");
    private final static Component TEXT_RGBA = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.rgba");

    private final TabGadget mainTabGadget;

    private final SliderGadget redSliderGadget, greenSliderGadget, blueSliderGadget;

    protected ColorEditorSideScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.color_edit"),
                152, 136);

        this.mainTabGadget = new TabGadget(0, 0, this.getGuiWidth(), this.getGuiHeight(),
                TabGadget.TabOrientation.RIGHT, 10);

        this.redSliderGadget = new SliderGadget(64, 20, 12, 4, 5);
        this.greenSliderGadget = new SliderGadget(64, 20, 30, 21, 20);
        this.blueSliderGadget = new SliderGadget(64, 20, 48, 10, 11);

        this.addGadgets(this.mainTabGadget, this.redSliderGadget, this.greenSliderGadget, this.blueSliderGadget);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background & gadgets
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        graphics.drawString(Minecraft.getInstance().font, TEXT_RGB, originX + 6, originY + 4, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_R, originX + 6, originY + 18, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_G, originX + 6, originY + 36, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_B, originX + 6, originY + 54, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_ALPHA, originX + 6, originY + 71, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_A, originX + 6, originY + 85, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_HEX, originX + 6, originY + 101, ScreenUtils.COLOR_WHITE);
    }
}
