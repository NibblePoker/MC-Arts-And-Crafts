package com.nibblepoker.artsandcrafts.interfaces.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Visually pleasing blank containers in Minecraft's original style as well as in tab-ish styles.
 * @version 3.0.0
 */
public class TabGadget extends NPGadget {
	private static final ResourceLocation TABS_TEXTURE = new ResourceLocation(ArtsAndCraftsMod.MOD_ID,"textures/gui/tabs.png");
	private static final int TEXTURE_WIDTH = 384;
	private static final int TEXTURE_HEIGHT = 384;

	public enum TabOrientation {
		LEFT,
		RIGHT,
		DOWN,
		UP,
		NONE;
	}

	public TabOrientation tabOrientation;

	public int tabColor;

	public TabGadget(int width, int height, TabOrientation tabOrientation) {
		this(0, 0, width, height, tabOrientation, 14);
	}

	public TabGadget(int width, int height, TabOrientation tabOrientation, int tabColor) {
		this(0, 0, width, height, tabOrientation, tabColor);
	}

	public TabGadget(int offsetX, int offsetY, int width, int height, TabOrientation tabOrientation) {
		this(offsetX, offsetY, width, height, tabOrientation, 14);
	}

	public TabGadget(int offsetX, int offsetY, int width, int height, TabOrientation tabOrientation, int tabColor) {
		super(width, height, offsetX, offsetY);
		this.tabOrientation = tabOrientation;
		this.tabColor = tabColor % 64;
	}

	@Override
	public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
							   int relativeOriginX, int relativeOriginY) {
		// Used to hide tabs when rendering and updating the size through methods.
		if(this.width == 0 || this.height == 0) {
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
				relativeOriginX, relativeOriginY,
				8, 8,
				textureOriginX, textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + 8, relativeOriginY,
				this.width - 16, 8,
				textureOriginX+8, textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + this.width - 8, relativeOriginY,
				8, 8,
				textureOriginX+((this.tabOrientation != TabOrientation.NONE) ? 16 : 40), textureOriginY,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);

		graphics.blit(TABS_TEXTURE,
				relativeOriginX, relativeOriginY + 8,
				8, this.height - 16,
				textureOriginX, textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + 8, relativeOriginY + 8,
				this.width - 16, this.height - 16,
				textureOriginX+8, textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + this.width - 8, relativeOriginY + 8,
				8, this.height - 16,
				textureOriginX+((this.tabOrientation != TabOrientation.NONE) ? 16 : 40), textureOriginY+8,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);

		if(this.tabOrientation == TabOrientation.NONE) {
			// We'll simply use the texture for "down" tabs.
			textureOriginY += 24;
		}

		graphics.blit(TABS_TEXTURE,
				relativeOriginX, relativeOriginY + this.height - 8,
				8, 8,
				textureOriginX, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + 8, relativeOriginY + this.height - 8,
				this.width - 16, 8,
				textureOriginX+8, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
		graphics.blit(TABS_TEXTURE,
				relativeOriginX + this.width - 8, relativeOriginY + this.height - 8,
				8, 8,
				textureOriginX+16, textureOriginY+16,
				8, 8,
				TEXTURE_WIDTH, TEXTURE_HEIGHT);
	}
}
