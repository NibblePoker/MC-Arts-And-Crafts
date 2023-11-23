package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class TextButtonGadget implements IGadget {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/buttons_legacy.png");

    public int posOffsetX, posOffsetY;
    public int width, height;
    public Component text;
    public boolean isDisabled;

    public TextButtonGadget(int posOffsetX, int posOffsetY, int width, int height, Component text) {
        this.posOffsetX = posOffsetX;
        this.posOffsetY = posOffsetY;
        //this.width = Math.max(Math.min(Math.abs(width), 20), 8);
        //this.height = Math.max(Math.min(Math.abs(height), 200), 8);
        this.width = width;
        this.height = height;
        this.text = text;
        this.isDisabled = false;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int textureBaseOffsetY = 20 * ((this.isDisabled ? 2 : (
                this.isMouseOver(mouseX, mouseY, parentOriginX, parentOriginY) ? 1 : 0
        )));

        graphics.blit(BACKGROUND_TEXTURE,
                parentOriginX + this.posOffsetX, parentOriginY + this.posOffsetY,
                this.width - 5, this.height - 3,
                0, textureBaseOffsetY,
                this.width - 5, this.height - 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                parentOriginX + this.posOffsetX + this.width - 5, parentOriginY + this.posOffsetY,
                5, this.height - 3,
                195, textureBaseOffsetY,
                5, this.height - 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                parentOriginX + this.posOffsetX, parentOriginY + this.posOffsetY + this.height - 3,
                this.width - 5, 3,
                0, textureBaseOffsetY + 17,
                this.width - 5, 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                parentOriginX + this.posOffsetX + this.width - 5,
                parentOriginY + this.posOffsetY + this.height - 3,
                5, 3,
                195, textureBaseOffsetY + 17,
                5, 3,
                256, 256);

        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.text,
                parentOriginX + this.posOffsetX + (this.width / 2),
                parentOriginY + this.posOffsetY + (this.height / 2),
                (this.isDisabled ? ScreenUtils.COLOR_TEXT_BUTTON_DISABLED : (
                        this.isMouseOver(mouseX, mouseY, parentOriginX, parentOriginY) ?
                                ScreenUtils.COLOR_TEXT_BUTTON_HOVER : ScreenUtils.COLOR_TEXT_BUTTON
                ))
        );
    }

    @Override
    public boolean isMouseOver(int normalizedX, int normalizedY) {
        return normalizedX >= this.posOffsetX && normalizedX < this.posOffsetX + this.width &&
                normalizedY >= this.posOffsetY && normalizedY < this.posOffsetY + this.height;
    }

    @Override
    public boolean onMouseClicked(int normalizedX, int normalizedY, int mouseButton, ItemStack currentlyCarriedItemStack) {
        if(this.isMouseOver(normalizedX, normalizedY) && !this.isDisabled) {
            this.playClickSound();
            return true;
        }
        return false;
    }

    public void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
