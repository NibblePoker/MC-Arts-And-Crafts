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

public class CanvasGadget extends NPGadget {
    private static final ResourceLocation CANVAS_BACKGROUND_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/canvas_background.png");
    private static final ResourceLocation CANVAS_BORDER_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/canvas_border.png");
    
    public boolean isModifiable;

    // Temp variables
    private int pixelSize;
    private int pixelPerAxisCount;

    // Image data
    private final NativeImage drawingImage;
    private ResourceLocation drawingResource;
    private boolean needsRefresh;
    private float opacity;

    public CanvasGadget(int posOffsetX, int posOffsetY, int width, int height) {
        this(width, height, posOffsetX, posOffsetY, null);
    }

    public CanvasGadget(int posOffsetX, int posOffsetY, int width, int height, NativeImage referencedImage) {
        super(width, height, posOffsetX, posOffsetY);
        this.isModifiable = false;

        // Temporary values, will be improved once zoom and pan is implemented.
        this.pixelPerAxisCount = 16;
        // Must not be zero !
        this.pixelSize = Math.max(1, this.width / this.pixelPerAxisCount);

        // Preparing the image data
        if(referencedImage == null) {
            this.drawingImage = new NativeImage(NativeImage.Format.RGBA,
                    this.pixelPerAxisCount, this.pixelPerAxisCount, true);
            this.drawingImage.fillRect(0, 0, this.pixelPerAxisCount, this.pixelPerAxisCount,
                    ScreenUtils.COLOR_TRANSPARENT);
        } else {
            this.drawingImage = referencedImage;
        }
        this.drawingResource = Minecraft.getInstance().getTextureManager().register(
                ArtsAndCraftsMod.MOD_ID + "/" + "tmp_editor_canvas_content", new DynamicTexture(this.drawingImage)
        );

        // Preparing other variables
        this.needsRefresh = false;
        this.opacity = 1.0F;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        // Background
        graphics.blit(CANVAS_BACKGROUND_TEXTURE,
                relativeOriginX, relativeOriginY,
                this.width, this.height,
                0, 0,
                this.pixelPerAxisCount, this.pixelPerAxisCount,
                64, 64);

        // Rendering the image
        graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
        graphics.blit(this.drawingResource,
                relativeOriginX, relativeOriginY,
                this.width, this.height,
                0, 0,
                this.pixelPerAxisCount, this.pixelPerAxisCount,
                this.pixelPerAxisCount, this.pixelPerAxisCount);

        // Rendering the pixel highlighter.
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if(this.isModifiable && this.isMouseOverRelative(relativeMouseX, relativeMouseY)) {
            int pixelX = Math.max(0, relativeMouseX / this.pixelSize);
            int pixelY = Math.max(0, relativeMouseY / this.pixelSize);

            //renderOutline
            graphics.fill(
                    relativeOriginX + (pixelX * this.pixelSize),
                    relativeOriginY + (pixelY * this.pixelSize),
                    relativeOriginX + ((pixelX + 1) * this.pixelSize),
                    relativeOriginY + ((pixelY + 1) * this.pixelSize),
                    FastColor.ARGB32.color(0xFF, 0xFF, 0x7F, 0x7F)
            );
        }

        // Rendering top-left border corner
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX - 4, relativeOriginY - 4,
                4, 4,
                0, 0,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX, relativeOriginY,
                4, 4,
                4, 4,
                4, 4,
                16, 16);

        // Rendering top-right border corner
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width, relativeOriginY - 4,
                4, 4,
                12, 0,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width - 4, relativeOriginY,
                4, 4,
                8, 4,
                4, 4,
                16, 16);

        // Rendering bottom left border corner
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX - 4, relativeOriginY + this.height,
                4, 4,
                0, 12,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX, relativeOriginY + this.height - 4,
                4, 4,
                4, 8,
                4, 4,
                16, 16);

        // Rendering bottom right border corner
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width, relativeOriginY + this.height,
                4, 4,
                12, 12,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width - 4, relativeOriginY + this.height - 4,
                4, 4,
                8, 8,
                4, 4,
                16, 16);

        // Rendering top-middle border
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX, relativeOriginY - 4,
                this.width, 4,
                4, 0,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + 4, relativeOriginY,
                this.width - 8, 4,
                8, 0,
                4, 4,
                16, 16);

        // Rendering bottom-middle border
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX, relativeOriginY + this.height,
                this.width, 4,
                4, 12,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + 4, relativeOriginY + this.height - 4,
                this.width - 8, 4,
                8, 12,
                4, 4,
                16, 16);

        // Rendering left-middle border
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX - 4, relativeOriginY,
                4, this.height,
                0, 4,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX, relativeOriginY + 4,
                4, this.height - 8,
                0, 8,
                4, 4,
                16, 16);

        // Rendering right-middle border
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width, relativeOriginY,
                4, this.height,
                12, 4,
                4, 4,
                16, 16);
        graphics.blit(CANVAS_BORDER_TEXTURE,
                relativeOriginX + this.width - 4, relativeOriginY + 4,
                4, this.height - 8,
                12, 8,
                4, 4,
                16, 16);
    }

    @Override
    public boolean isMouseOverRelative(int relativeMouseX, int relativeMouseY) {
        return relativeMouseX >= 1 && relativeMouseX < this.width - 1 && relativeMouseY >= 1 && relativeMouseY < this.height - 1;
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

    public boolean setColor(int parentX, int parentY, int newColor) {
        return this.setColorRelative(parentX - this.offsetX, parentY - this.offsetY, newColor);
    }

    private boolean setColorRelative(int relativeX, int relativeY, int newColor) {
        if(!this.isMouseOverRelative(relativeX, relativeY)) {
            return false;
        }

        int pixelX = Math.min(this.pixelPerAxisCount - 1, Math.max(0, relativeX / this.pixelSize));
        int pixelY = Math.min(this.pixelPerAxisCount - 1, Math.max(0, relativeY / this.pixelSize));
        //System.out.println(pixelX + ":" + pixelY);

        if(this.drawingImage.getPixelRGBA(pixelX, pixelY) != newColor) {
            // Converting the given ARGB to canvas' ABGR.
            this.drawingImage.setPixelRGBA(pixelX, pixelY, ScreenUtils.swapRGB(newColor));
            this.needsRefresh = true;
            return true;
        }

        return true;
    }

    public int getColor(int parentX, int parentY) {
        return this.getColorRelative(parentX - this.offsetX, parentY - this.offsetY);
    }

    private int getColorRelative(int relativeX, int relativeY) {
        if(this.isMouseOverRelative(relativeX, relativeY)) {
            int pixelX = Math.min(this.pixelPerAxisCount - 1, Math.max(0, relativeX / this.pixelSize));
            int pixelY = Math.min(this.pixelPerAxisCount - 1, Math.max(0, relativeY / this.pixelSize));
            // Converting and returning the canvas' ABGR to a semi-standard ARGB.
            return ScreenUtils.swapRGB(this.drawingImage.getPixelRGBA(pixelX, pixelY));
        }

        return ScreenUtils.COLOR_TRANSPARENT;
    }

    // Getters & setters
    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.min(1.0F, Math.max(0.0F, opacity));
    }

    public NativeImage getImage() {
        return this.drawingImage;
    }
}
