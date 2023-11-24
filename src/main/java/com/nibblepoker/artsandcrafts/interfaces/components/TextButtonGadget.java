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

public class TextButtonGadget extends NPGadget {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/buttons_legacy.png");
    public Component text;
    public boolean isDisabled;

    public TextButtonGadget(int posOffsetX, int posOffsetY, int width, int height, Component text) {
        super(width, height, posOffsetX, posOffsetY);
        this.text = text;
        this.isDisabled = false;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        int textureBaseOffsetY = 20 * ((this.isDisabled ? 2 : (
                this.isMouseOverRelative(relativeMouseX, relativeMouseY) ? 1 : 0
        )));

        graphics.blit(BACKGROUND_TEXTURE,
                relativeOriginX, relativeOriginY,
                this.width - 5, this.height - 3,
                0, textureBaseOffsetY,
                this.width - 5, this.height - 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                relativeOriginX + this.width - 5, relativeOriginY,
                5, this.height - 3,
                195, textureBaseOffsetY,
                5, this.height - 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                relativeOriginX, relativeOriginY + this.height - 3,
                this.width - 5, 3,
                0, textureBaseOffsetY + 17,
                this.width - 5, 3,
                256, 256);
        graphics.blit(BACKGROUND_TEXTURE,
                relativeOriginX + this.width - 5,
                relativeOriginY + this.height - 3,
                5, 3,
                195, textureBaseOffsetY + 17,
                5, 3,
                256, 256);

        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.text,
                relativeOriginX + (this.width / 2),
                relativeOriginY + (this.height / 2),
                (this.isDisabled ? ScreenUtils.COLOR_TEXT_BUTTON_DISABLED : (
                        this.isMouseOverRelative(relativeMouseX, relativeMouseY) ?
                                ScreenUtils.COLOR_TEXT_BUTTON_HOVER : ScreenUtils.COLOR_TEXT_BUTTON
                ))
        );
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
