package com.nibblepoker.artsandcrafts.interfaces.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * This interface aims to fix some of the atrocious behavior that can be found in modern Minecraft's UI code.
 * The code worked perfectly back in 1.12, why did you mess it up so badly ?
 * I can't even have a button stay on screen after I resize the window by 1 pixel...
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
	void render(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, int parentOriginX, int parentOriginY);

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
		return this.onMouseClicked(
				((int) absoluteClickX) - parentOriginX,
				((int) absoluteClickY) - parentOriginY,
				mouseButton, currentlyCarriedItemStack
		);
	}

	/**
	 * Processes any mouse click that happens in the gadget.
	 * @param normalizedX Normalized cursor's X position relative to parent's origin.
	 * @param normalizedY Normalized cursor's Y position relative to parent's origin.
	 * @param mouseButton Mouse's button pressed.
	 * @param currentlyCarriedItemStack Copy of the item stack currently held on the cursor.
	 * @return <code>true</code> if the gadget was clicked, <code>false</code> otherwise.
	 */
	default boolean onMouseClicked(int normalizedX, int normalizedY, int mouseButton, ItemStack currentlyCarriedItemStack) {
		return false;
	}

	default boolean isMouseOver(int mouseX, int mouseY, int parentOriginX, int parentOriginY) {
		return this.isMouseOver(mouseX - parentOriginX, mouseY - parentOriginY);
	}

	default boolean isMouseOver(int normalizedX, int normalizedY) {
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

	default void tick() {}
}
