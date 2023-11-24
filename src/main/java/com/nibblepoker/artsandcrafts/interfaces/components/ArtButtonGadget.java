package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

/**
 * Simple button with an image instead of text.
 * @version 2.0.0
 */
public class ArtButtonGadget extends NPGadget {
    private static final ResourceLocation BUTTONS_TEXTURE = new ResourceLocation(ArtsAndCraftsMod.MOD_ID,"textures/gui/buttons.png");

    private final int textureOriginX, textureOriginY;
    public boolean isDisabled;

    public ArtButtonGadget(EArtButtonType buttonType, int posOffsetX, int posOffsetY) {
        this(posOffsetX, posOffsetY, buttonType.width, buttonType.height, buttonType.textureOriginX, buttonType.textureOriginY);
    }

    public ArtButtonGadget(int posOffsetX, int posOffsetY, int width, int height, int textureOriginX, int textureOriginY) {
        super(width, height, posOffsetX, posOffsetY);
        this.textureOriginX = textureOriginX;
        this.textureOriginY = textureOriginY;
        this.isDisabled = false;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int textureOffsetY = this.height * ((this.isDisabled ? 2 : (
                this.isMouseOverRelative(relativeMouseX, relativeMouseY) ? 1 : 0
        )));

        graphics.blit(BUTTONS_TEXTURE,
                relativeOriginX, relativeOriginY,
                this.width, this.height,
                this.textureOriginX, this.textureOriginY + textureOffsetY,
                this.width, this.height,
                256, 256);
    }

    @Override
    public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        if(this.isMouseOverRelative(relativeMouseX, relativeMouseY) && !this.isDisabled) {
            this.playClickSound();
            return true;
        }
        return false;
    }

    public void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
