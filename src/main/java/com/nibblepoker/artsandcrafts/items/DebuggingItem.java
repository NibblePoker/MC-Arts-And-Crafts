package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebuggingItem extends Item {
	public DebuggingItem(Properties settings) {
		super(settings);
	}
	
	/**
	 * Triggered when the item is used on any inventory-like slot while the item is being held at the cursor.
	 * @param stack ???
	 * @param slot ???
	 * @param clickType Type of click used when the call was triggered, refers mostly to the mouse.
	 * @param player ???
	 * @return <i>true</i> if the event was handled, <i>false</i> otherwise and if any other handler should be called.
	 */
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
		ArtsAndCraftsMod.LOGGER.info("DebugJoystickItem.onStackClicked(...)");
		ArtsAndCraftsMod.LOGGER.info("> stack: "+stack.toString());
		ArtsAndCraftsMod.LOGGER.info("> slot: "+slot.toString());
		ArtsAndCraftsMod.LOGGER.info("> clickType: "+clickType.toString());
		ArtsAndCraftsMod.LOGGER.info("> player: "+player.toString());
		ArtsAndCraftsMod.LOGGER.info("");
		return false;
	}
	
	/**
	 * Function called when the item is clicked on with the mouse cursor or picked up from any "inventory-like" storage.
	 * @param stack ???
	 * @param otherStack ???
	 * @param slot ???
	 * @param clickType Type of click used when the call was triggered, refers mostly to the mouse.
	 * @param player ???
	 * @param cursorStackReference ???
	 * @return <i>true</i> if the event was handled, <i>false</i> otherwise and if any other handler should be called.
	 */
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		ArtsAndCraftsMod.LOGGER.info("DebugJoystickItem.onClicked(...)");
		ArtsAndCraftsMod.LOGGER.info("> stack: "+stack.toString());
		ArtsAndCraftsMod.LOGGER.info("> slot: "+slot.toString());
		ArtsAndCraftsMod.LOGGER.info("> clickType: "+clickType.toString());
		ArtsAndCraftsMod.LOGGER.info("> player: "+player.toString());
		//ArtsAndCraftsMod.LOGGER.info("> player: "+player.readNbt());
		ArtsAndCraftsMod.LOGGER.info("");
		
		ArtsAndCraftsMod.LOGGER.info("> cursorStackReference: "+cursorStackReference.toString());
		return false;
	}
	
	/**
	 * Called when the item is used with a right-click in the world.
	 * This function may be called twice for the client's world and the server's world.
	 * @param world N/A
	 * @param user N/A
	 * @param hand Hand in which the activated item resides.
	 * @return N/A
	 */
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ArtsAndCraftsMod.LOGGER.info("DebugJoystickItem.use(...)");
		ArtsAndCraftsMod.LOGGER.info("> world: "+world.toString());
		ArtsAndCraftsMod.LOGGER.info("> user: "+user.toString());
		ArtsAndCraftsMod.LOGGER.info("> hand: "+hand.toString());
		ArtsAndCraftsMod.LOGGER.info("");
		
		ItemStack itemStack = user.getItemInHand(hand);
		return InteractionResultHolder.fail(itemStack);
	}
}
