package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class CanvasGadget implements IGadget {
    private static final ResourceLocation CANVAS_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/canvas_editor.png");

    public int posOffsetX, posOffsetY;
    public int width, height;
    public boolean isModifiable;

    private int pixelSize;
    private int pixelPerAxisCount;

    // Image data
    private final NativeImage drawingImage;
    private ResourceLocation drawingResource;
    private boolean needsRefresh;

    public CanvasGadget(int posOffsetX, int posOffsetY, int width, int height) {
        this.posOffsetX = posOffsetX;
        this.posOffsetY = posOffsetY;
        this.width = width;
        this.height = height;
        this.isModifiable = false;

        // Temporary values, will be improved once zoom and pan is implemented.
        this.pixelSize = 8;
        this.pixelPerAxisCount = 16;

        // Preparing the image data
        this.drawingImage = new NativeImage(NativeImage.Format.RGBA,
                this.pixelPerAxisCount, this.pixelPerAxisCount, true);
        this.drawingImage.fillRect(0, 0, this.pixelPerAxisCount, this.pixelPerAxisCount,
                ScreenUtils.COLOR_TRANSPARENT);
        this.drawingResource = Minecraft.getInstance().getTextureManager().register(
                ArtsAndCraftsMod.MOD_ID + "/" + "tmp_editor_canvas_content", new DynamicTexture(this.drawingImage)
        );
        this.needsRefresh = false;
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

        // Rendering the actual image
        graphics.blit(this.drawingResource,
                absCanvasInnerOriginX, absCanvasInnerOriginY,
                128, 128,
                0, 0,
                this.pixelPerAxisCount, this.pixelPerAxisCount,
                this.pixelPerAxisCount, this.pixelPerAxisCount);

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

    @Override
    public void tick() {
        if(this.needsRefresh) {
            // This probably isn't the best way to refresh it !
            // However, I couldn't update the "DynamicTexture" with the "NativeImage".
            // I have no idea why, it just crashed.
            this.drawingResource = Minecraft.getInstance().getTextureManager().register(
                    ArtsAndCraftsMod.MOD_ID + "/" + "tmp_editor_canvas_content", new DynamicTexture(this.drawingImage)
            );
            this.needsRefresh = false;
        }
    }

    public boolean setColor(int normalizedX, int normalizedY, int newColor) {
        if(!this.isMouseOver(normalizedX, normalizedY)) {
            return false;
        }

        int pixelX = Math.min(this.pixelPerAxisCount - 1, Math.max(0, (normalizedX - this.posOffsetX - 1) / this.pixelSize));
        int pixelY = Math.min(this.pixelPerAxisCount - 1, Math.max(0, (normalizedY - this.posOffsetY - 1) / this.pixelSize));
        //System.out.println(pixelX + ":" + pixelY);

        if(this.drawingImage.getPixelRGBA(pixelX, pixelY) != newColor) {
            // Converting the given ARGB to canvas' ABGR.
            this.drawingImage.setPixelRGBA(pixelX, pixelY, ScreenUtils.swapRGB(newColor));
            this.needsRefresh = true;
            return true;
        }

        return true;
    }


    public int getColor(int normalizedX, int normalizedY) {
        if(this.isMouseOver(normalizedX, normalizedY)) {
            int pixelX = Math.min(this.pixelPerAxisCount - 1, Math.max(0, (normalizedX - this.posOffsetX - 1) / this.pixelSize));
            int pixelY = Math.min(this.pixelPerAxisCount - 1, Math.max(0, (normalizedY - this.posOffsetY - 1) / this.pixelSize));
            // Converting and returning the canvas' ABGR to a semi-standard ARGB.
            return ScreenUtils.swapRGB(this.drawingImage.getPixelRGBA(pixelX, pixelY));
        }

        return ScreenUtils.COLOR_TRANSPARENT;
    }
}
