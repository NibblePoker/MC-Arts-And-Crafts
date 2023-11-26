package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * @version 1.0.0
 */
public class SliderGadget extends NPGadget {
    private static final ResourceLocation SLIDER_BASE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/slider_base.png");
    private static final ResourceLocation SLIDER_TAB_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/slider_tab.png");

    private final int baseColor, tabColor;

    // This value is stored as a simple integer since I can't be bothered
    //  to handle non-int positions in interfaces.
    private int value;

    // Used to handle click and dragging events when the user goes out of the
    //  gadget's bounds.
    private boolean hasCapturedMouseClick;

    public SliderGadget(int width, int offsetX, int offsetY) {
        this(width, offsetX, offsetY, 14, 14);
    }

    public SliderGadget(int width, int offsetX, int offsetY, int baseColor, int tabColor) {
        super(width, 15, offsetX, offsetY);
        this.baseColor = baseColor;
        this.tabColor = tabColor;
        this.value = 0;
        this.hasCapturedMouseClick = false;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int baseTextureOffsetX = ((this.baseColor % 8) * 15);
        int baseTextureOffsetY = ((int) Math.floor((float) this.baseColor / 8.0F) * 10) +
                (this.isMouseOverRelative(relativeMouseX, relativeMouseY) ? 5 : 0);

        int tabTextureOffsetX = ((this.tabColor % 8) * 12) +
                (this.isMouseOverRelative(relativeMouseX, relativeMouseY) ? 0 : 6);
        int tabTextureOffsetY = ((int) Math.floor((float) this.tabColor / 8.0F) * 15);

        graphics.blit(SLIDER_BASE_TEXTURE,
                relativeOriginX, relativeOriginY + 5,
                5, 5,
                baseTextureOffsetX, baseTextureOffsetY,
                5, 5,
                120, 80);
        graphics.blit(SLIDER_BASE_TEXTURE,
                relativeOriginX + 5, relativeOriginY + 5,
                this.width - 10, 5,
                baseTextureOffsetX + 5, baseTextureOffsetY,
                5, 5,
                120, 80);
        graphics.blit(SLIDER_BASE_TEXTURE,
                relativeOriginX + this.width - 5, relativeOriginY + 5,
                5, 5,
                baseTextureOffsetX + 10, baseTextureOffsetY,
                5, 5,
                120, 80);

        graphics.blit(SLIDER_TAB_TEXTURE,
                relativeOriginX + this.value, relativeOriginY,
                6, 15,
                tabTextureOffsetX, tabTextureOffsetY,
                6, 15,
                96, 120);
    }

    @Override
    public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        if(this.isMouseOverRelative(relativeMouseX, relativeMouseY) && clickButton == 0) {
            this.handleValueChange(relativeMouseX, relativeMouseY);
            this.hasCapturedMouseClick = true;
        }
        return this.hasCapturedMouseClick;
    }

    @Override
    public boolean mouseDraggedRelative(int relativeMouseX, int relativeMouseY, int clickButton, double deltaX, double deltaY) {
        if(this.hasCapturedMouseClick) {
            this.handleValueChange(relativeMouseX, relativeMouseY);
        }
        return this.hasCapturedMouseClick;
    }

    @Override
    public boolean mouseReleasedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        if(this.hasCapturedMouseClick) {
            this.handleValueChange(relativeMouseX, relativeMouseY);
            this.hasCapturedMouseClick = false;
            return true;
        }
        return false;
    }

    public void handleValueChange(int relativeMouseX, int relativeMouseY) {
        this.setValue(relativeMouseX);
    }

    private int getMaxValue() {
        return Math.max(0, this.width - 6);
    }

    private void setValue(int newValue) {
        this.value = Math.max(0, Math.min(this.width - 6, newValue));
    }

    public int getMappedValue(int min, int max) {
        return Math.max(min, Math.min(max, (int)(min + (((float) this.value / (float) this.getMaxValue()) * (max - min)))));
    }

    public void setMappedValue(int min, int max, int value) {
        //this.value = Math.max(min, Math.min(max, (int) (((float) max - (float) min) / ((float) value - (float) min)) * this.getMaxValue()));
        this.value = (int) (((double) (value - min) / (max - min)) * this.getMaxValue());
    }

    @Override
    public boolean isMouseOverRelative(int relativeMouseX, int relativeMouseY) {
        return super.isMouseOverRelative(relativeMouseX, relativeMouseY) || this.hasCapturedMouseClick;
    }
}
