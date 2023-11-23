package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class CanvasGadget implements IGadget {
    private static final ResourceLocation CANVAS_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/canvas_editor.png");

    public int posOffsetX, posOffsetY;
    public int width, height;
    public boolean isModifiable;

    private int pixelSize;

    public CanvasGadget(int posOffsetX, int posOffsetY, int width, int height) {
        this.posOffsetX = posOffsetX;
        this.posOffsetY = posOffsetY;
        this.width = width;
        this.height = height;
        this.isModifiable = false;

        // Temporary values, will be improved once zoom and pan is implemented.
        this.pixelSize = 8;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        // Background
        graphics.blit(CANVAS_TEXTURE,
                parentOriginX + this.posOffsetX, parentOriginY + this.posOffsetY,
                130, 130,
                0, 0,
                130, 130,
                130, 130);

        // Temp values for rendering.
        int absCanvasInnerOriginX = parentOriginX + this.posOffsetX + 1;
        int absCanvasInnerOriginY = parentOriginY + this.posOffsetY + 1;

        // Pixel target.
        if(this.isModifiable && this.isMouseOver(mouseX, mouseY, parentOriginX, parentOriginY)) {
            int pixelX = Math.max(0, (mouseX - absCanvasInnerOriginX) / this.pixelSize);
            int pixelY = Math.max(0, (mouseY - absCanvasInnerOriginY) / this.pixelSize);

            graphics.fill(
                    absCanvasInnerOriginX + (pixelX * this.pixelSize),
                    absCanvasInnerOriginY + (pixelY * this.pixelSize),
                    absCanvasInnerOriginX + ((pixelX + 1) * this.pixelSize),
                    absCanvasInnerOriginY + ((pixelY + 1) * this.pixelSize),
                    FastColor.ARGB32.color(0xFF, 0xFF, 0x7F, 0x7F)
            );
        }
    }

    @Override
    public boolean isMouseOver(int normalizedX, int normalizedY) {
        return normalizedX > this.posOffsetX && normalizedX < this.posOffsetX + this.width - 1 &&
                normalizedY > this.posOffsetY && normalizedY < this.posOffsetY + this.height - 1;
    }
}
