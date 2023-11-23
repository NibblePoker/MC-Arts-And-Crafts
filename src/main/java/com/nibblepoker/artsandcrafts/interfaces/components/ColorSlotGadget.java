package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;

public class ColorSlotGadget implements IGadget {
    private static final ResourceLocation COLOR_ACTIVE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_active.png");
    private static final ResourceLocation COLOR_PALETTE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_palette.png");

    private final ResourceLocation usedTexture;

    public int posOffsetX, posOffsetY;
    private final int width, height;
    public int color;

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType) {
        this(posOffsetX, posOffsetY, slotType, FastColor.ARGB32.color(0x00, 0x00, 0x00, 0x00));
    }

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType, int color) {
        this.posOffsetX = posOffsetX;
        this.posOffsetY = posOffsetY;
        this.width = slotType.width;
        this.height = slotType.height;
        this.color = color;

        // Referencing the correct texture.
        this.usedTexture = slotType == EColorSlotGadgetType.SMALL ? COLOR_PALETTE_TEXTURE : COLOR_ACTIVE_TEXTURE;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        graphics.blit(this.usedTexture,
                parentOriginX + this.posOffsetX, parentOriginY + this.posOffsetY,
                this.width, this.height,
                0, 0,
                this.width, this.height,
                this.width, this.height);

        graphics.fill(
                parentOriginX + this.posOffsetX + 1,
                parentOriginY + this.posOffsetY + 1,
                parentOriginX + this.posOffsetX + this.width - 1,
                parentOriginY + this.posOffsetY + this.height - 1,
                this.color);
    }

    @Override
    public boolean isMouseOver(int normalizedX, int normalizedY) {
        return normalizedX >= this.posOffsetX && normalizedX < this.posOffsetX + this.width &&
                normalizedY >= this.posOffsetY && normalizedY < this.posOffsetY + this.height;
    }
}
