package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

/**
 * Temporary replacement for Minecraft's standard text input Widget.
 * I'll extend it later once I can figure them out and use them without pulling my hair out.
 */
public class TextInputGadget extends NPGadget {
    private static final ResourceLocation TEXT_INPUT_BACKGROUND = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/text_input.png");

    public String content;
    public boolean isEditable;
    public boolean isCapturingInputs;

    public TextInputGadget(int width, int height, int offsetX, int offsetY) {
        super(width, height, offsetX, offsetY);
        this.content = "";
        this.isEditable = true;
        this.isCapturingInputs = false;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY, int relativeOriginX, int relativeOriginY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int textureOriginX = 0;
        int textureOriginY = 0;
        if(!this.isEditable) {
            textureOriginX = 15;
        } else if(this.isCapturingInputs) {
            textureOriginY = 15;
        }

        // Rendering the background
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX, relativeOriginY, 5, 5, textureOriginX,
                textureOriginY, 5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + 5, relativeOriginY, this.width - 10,
                5, textureOriginX + 5, textureOriginY, 5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + this.width - 5, relativeOriginY, 5,
                5, textureOriginX + 10, textureOriginY, 5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX, relativeOriginY + 5, 5, this.height - 10,
                textureOriginX, textureOriginY + 5, 5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + 5, relativeOriginY + 5,
                this.width - 10, this.height - 10, textureOriginX + 5, textureOriginY + 5,
                5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + this.width - 5, relativeOriginY + 5,
                5, this.height - 10, textureOriginX + 10, textureOriginY + 5,
                5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX, relativeOriginY + this.height - 5, 5,
                5, textureOriginX, textureOriginY + 10, 5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + 5, relativeOriginY + this.height - 5,
                this.width - 10, 5, textureOriginX + 5, textureOriginY + 10,
                5, 5, 30, 30);
        graphics.blit(TEXT_INPUT_BACKGROUND, relativeOriginX + this.width - 5,
                relativeOriginY + this.height - 5, 5, 5, textureOriginX + 10,
                textureOriginY + 10, 5, 5, 30, 30);

        // Crudely drawing the text
        ScreenUtils.drawUnshadedCenteredString(graphics, Minecraft.getInstance().font, this.content,
                relativeOriginX + (this.width / 2), relativeOriginY + (this.height / 2) + 1,
                ScreenUtils.COLOR_TEXT_INPUT);
    }

    @Override
    public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        if(this.isMouseOverRelative(relativeMouseX, relativeMouseY)) {
            this.isCapturingInputs = clickButton == 0;
            if(this.isCapturingInputs) {
                playClickSound();
            } else {
                playDoneSound();
            }
        } else if(this.isCapturingInputs) {
            this.isCapturingInputs = false;
            playDoneSound();
        }
        // Preventing other fields from being deselected.
        return false;
    }

    @Override
    public boolean charTyped(char charTyped, int unknown) {
        if(this.isCapturingInputs) {
            if(!this.onPreCharTyped(charTyped, unknown)) {
                return false;
            }
            this.content += charTyped;
            return this.onPostCharTyped(charTyped, unknown);
        }
        return false;
    }

    public boolean onPreCharTyped(char charTyped, int unknown) {
        return true;
    }

    public boolean onPostCharTyped(char charTyped, int unknown) {
        return true;
    }

    private static void playDoneSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_WORK_CARTOGRAPHER, 1.0F));
    }

    private static void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
