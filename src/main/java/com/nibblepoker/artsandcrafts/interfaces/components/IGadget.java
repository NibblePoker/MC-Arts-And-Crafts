package com.nibblepoker.artsandcrafts.interfaces.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * TODO
 */
public interface IGadget {
	/**
	 * TODO.<br>
	 * @param graphics
	 * @param partialTick
	 * @param mouseX
	 * @param mouseY
	 * @param parentOriginX
	 * @param parentOriginY
	 */
	void renderBackground(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY);
	
	/**
	 * Processes any mouse click that happens in the gadget.
	 * @param absoluteClickX Non-normalized cursor's X position.
	 * @param absoluteClickY Non-normalized cursor's Y position.
	 * @param parentOriginX Cursor's X position relative to the parent's X origin position.
	 * @param parentOriginY Cursor's Y position relative to the parent's Y origin position.
	 * @param mouseButton Mouse's button pressed.
	 * @param currentlyCarriedItemStack Copy of the item stack currently held on the cursor.
	 * @return <code>true</code> if the gadget was clicked, <code>false</code> otherwise.
	 */
	default boolean onMouseClicked(double absoluteClickX, double absoluteClickY, int parentOriginX, int parentOriginY, int mouseButton, ItemStack currentlyCarriedItemStack) {
		return false;
	}
	
	/**
	 * ???
	 * @param posX
	 * @param posY
	 * @param verticalDistance
	 * @return <code>true</code> if the gadget was scrolled, <code>false</code> otherwise.
	 */
	default boolean mouseScrolled(double posX, double posY, double verticalDistance) {
		return false;
	}
}
