package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class ArtButtonGadget implements IGadget {
    private static final ResourceLocation BUTTONS_TEXTURE = new ResourceLocation(ArtsAndCraftsMod.MOD_ID,"textures/gui/buttons.png");

    public int posOffsetX, posOffsetY;
    public int width, height;
    private final int textureOriginX, textureOriginY;

    public ArtButtonGadget(EArtButtonType buttonType, int posOffsetX, int posOffsetY) {
        this(posOffsetX, posOffsetY, buttonType.width, buttonType.height, buttonType.textureOriginX, buttonType.textureOriginY);
    }

    public ArtButtonGadget(int posOffsetX, int posOffsetY, int width, int height, int textureOriginX, int textureOriginY) {
        this.posOffsetX = posOffsetX;
        this.posOffsetY = posOffsetY;
        this.width = width;
        this.height = height;
        this.textureOriginX = textureOriginX;
        this.textureOriginY = textureOriginY;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blit(BUTTONS_TEXTURE,
                parentOriginX + this.posOffsetX, parentOriginY + this.posOffsetY,
                this.width, this.height,
                this.textureOriginX, this.textureOriginY + (
                        this.isMouseOver(mouseX, mouseY, parentOriginX, parentOriginY) ? this.height : 0),
                this.width, this.height,
                256, 256);
    }

    @Override
    public boolean isMouseOver(int normalizedX, int normalizedY) {
        return normalizedX >= this.posOffsetX && normalizedX < this.posOffsetX + this.width &&
                normalizedY >= this.posOffsetY && normalizedY < this.posOffsetY + this.height;
    }

    @Override
    public boolean onMouseClicked(int normalizedX, int normalizedY, int mouseButton, ItemStack currentlyCarriedItemStack) {
        if(this.isMouseOver(normalizedX, normalizedY)) {
            this.playClickSound();
            return true;
        }
        return false;
    }

    protected void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
