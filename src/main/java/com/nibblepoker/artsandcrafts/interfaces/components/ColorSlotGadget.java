package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ColorSlotGadget extends NPGadget {
    private static final ResourceLocation COLOR_ACTIVE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_active.png");
    private static final ResourceLocation COLOR_PALETTE_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/editor_color_palette.png");

    private final ResourceLocation usedTexture;

    public int color;

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType) {
        this(posOffsetX, posOffsetY, slotType, ScreenUtils.COLOR_TRANSPARENT);
    }

    public ColorSlotGadget(int posOffsetX, int posOffsetY, EColorSlotGadgetType slotType, int color) {
        super(slotType.width, slotType.height, posOffsetX, posOffsetY);
        this.color = color;

        // Referencing the correct texture.
        this.usedTexture = slotType == EColorSlotGadgetType.SMALL ? COLOR_PALETTE_TEXTURE : COLOR_ACTIVE_TEXTURE;
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
    }
}
