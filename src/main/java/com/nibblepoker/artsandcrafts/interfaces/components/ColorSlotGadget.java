package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class ColorSlotGadget extends NPGadget {
    private static final ResourceLocation COLOR_ACTIVE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_active.png");
    private static final ResourceLocation COLOR_PALETTE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_palette.png");
    private static final ResourceLocation TARGET_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_target.png");

    private final ResourceLocation usedTexture;

    public int color;

    public boolean drawTarget;

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType) {
        this(posOffsetX, posOffsetY, slotType, ScreenUtils.COLOR_TRANSPARENT);
    }

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType, int color) {
        super(slotType.width, slotType.height, posOffsetX, posOffsetY);
        this.color = color;

        // Referencing the correct texture.
        this.usedTexture = slotType == EColorSlotGadgetType.SMALL ? COLOR_PALETTE_TEXTURE : COLOR_ACTIVE_TEXTURE;
        this.drawTarget = false;
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        graphics.blit(this.usedTexture,
                relativeOriginX, relativeOriginY,
                this.width, this.height,
                0, 0,
                this.width, this.height,
                this.width, this.height);

        graphics.fill(
                relativeOriginX + 1, relativeOriginY + 1,
                relativeOriginX + this.width - 1, relativeOriginY + this.height - 1,
                this.color);

        if(this.drawTarget) {
            graphics.blit(TARGET_TEXTURE,
                    relativeOriginX - 2, relativeOriginY - 2,
                    4, 4, 0, 0, 4, 4, 8, 8);
            graphics.blit(TARGET_TEXTURE,
                    relativeOriginX + this.width - 2, relativeOriginY - 2,
                    4, 4, 4, 0, 4, 4, 8, 8);
            graphics.blit(TARGET_TEXTURE,
                    relativeOriginX - 2, relativeOriginY + this.height - 2,
                    4, 4, 0, 4, 4, 4, 8, 8);
            graphics.blit(TARGET_TEXTURE,
                    relativeOriginX + this.width - 2, relativeOriginY + this.height - 2,
                    4, 4, 4, 4, 4, 4, 8, 8);
        }
    }

    public int getR() {
        return FastColor.ARGB32.red(this.color);
    }

    public int getG() {
        return FastColor.ARGB32.green(this.color);
    }

    public int getB() {
        return FastColor.ARGB32.blue(this.color);
    }

    public int getA() {
        return FastColor.ARGB32.alpha(this.color);
    }
}
