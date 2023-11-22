package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Ree !
 * @version 2.1.0
 */
public class TabGadget implements IGadget {
	private static final ResourceLocation TABS_TEXTURE = new ResourceLocation(ArtsAndCraftsMod.MOD_ID,"textures/gui/tabs.png");
	private static final int TEXTURE_WIDTH = 384;
	private static final int TEXTURE_HEIGHT = 384;

	public int tabOffsetX, tabOffsetY;

	public int tabWidth, tabHeight;

	public TabOrientation tabOrientation;

	public enum TabOrientation {
		LEFT,
		RIGHT,
		DOWN,
		UP,
		NONE;
	}

	public int tabColor;

	public TabGadget(TabOrientation tabOrientation) {
		this(0, 0, 0, 0, tabOrientation, 14);
	}

	public TabGadget(TabOrientation tabOrientation, int tabColor) {
		this(0, 0, 0, 0, tabOrientation, tabColor);
	}

	public TabGadget(int width, int height, TabOrientation tabOrientation) {
		this(0, 0, width, height, tabOrientation, 14);
	}

	public TabGadget(int width, int height, TabOrientation tabOrientation, int tabColor) {
		this(0, 0, width, height, tabOrientation, tabColor);
	}
	
	public TabGadget(int offsetX, int offsetY, int width, int height, TabOrientation tabOrientation, int tabColor) {
		this.tabOffsetX = offsetX;
		this.tabOffsetY = offsetY;
		this.tabWidth = width;
		this.tabHeight = height;
		this.tabOrientation = tabOrientation;
		this.tabColor = tabColor % 64;
	}

	@Override
	public void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
		this.renderManualBackground(graphics,
				parentOriginX + this.tabOffsetX, parentOriginY + this.tabOffsetY,
				this.tabWidth, this.tabHeight);
	}

	public void renderManualBackground(GuiGraphics graphics, int originX, int originY, int renderWidth, int renderHeight) {
		// Used to hide tabs when rendering and updating the size through methods.
		if(renderWidth == 0 || renderHeight == 0) {
			return;
		}

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TABS_TEXTURE);

		// Getting the top-left corner of the 24x24 texture part that will be used as the tab's background.
		int textureOriginX = ((this.tabColor % 8) * 48) +
				(this.tabOrientation == TabOrientation.RIGHT || this.tabOrientation == TabOrientation.UP ? 24 : 0);
		int textureOriginY = ((int) Math.floor((float) this.tabColor / 8.0F) * 48) +
				(this.tabOrientation == TabOrientation.DOWN || this.tabOrientation == TabOrientation.UP ? 24 : 0);

		// Rendering the tab's background
		graphics.blit(TABS_TEXTURE,
				originX, originY,
				8, 8,
				textureOriginX, textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + 8, originY,
				renderWidth - 16, 8,
				textureOriginX+8, textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + renderWidth - 8, originY,
				8, 8,
				textureOriginX+((this.tabOrientation != TabOrientation.NONE) ? 16 : 40), textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);

		graphics.blit(TABS_TEXTURE,
				originX, originY + 8,
				8, renderHeight - 16,
				textureOriginX, textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + 8, originY + 8,
				renderWidth - 16, renderHeight - 16,
				textureOriginX+8, textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + renderWidth - 8, originY + 8,
				8, renderHeight - 16,
				textureOriginX+((this.tabOrientation != TabOrientation.NONE) ? 16 : 40), textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);

		if(this.tabOrientation == TabOrientation.NONE) {
			// We'll simply use the texture for "down" tabs.
			textureOriginY += 24;
		}

		graphics.blit(TABS_TEXTURE,
				originX, originY + renderHeight - 8,
				8, 8,
				textureOriginX, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + 8, originY + renderHeight - 8,
				renderWidth - 16, 8,
				textureOriginX+8, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				originX + renderWidth - 8, originY + renderHeight - 8,
				8, 8,
				textureOriginX+16, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
	}
}
