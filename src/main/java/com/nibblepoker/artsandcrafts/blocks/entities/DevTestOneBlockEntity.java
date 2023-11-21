package com.nibblepoker.artsandcrafts.blocks.entities;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DevTestOneBlockEntity extends BlockEntity {
	// Max amount of ticks to wait before sending the first data request and any subsequent ones.
	private static final long RECURRENT_SPONTANEOUS_DATA_REQUESTS_DELAY = 50;

	public ItemStack givenItemStack = ItemStack.EMPTY;

	// We create a random number of ticks before the clients spontaneously requests data on the displayed item.
	// This solves the issue of the client not knowing this info when the block is loaded but wasn't changed.
	// The main reason behind the random call is to help somewhat stagger the packets that will be sent to the server
	//  if the client loads a chunk with a massive amount of this tile entity.
	// The value is an amount of ticks left before the call is made. (20 ticks per seconds normally)
	private long lastClientSpontaneousDataRequest;

	private boolean hasClientReceivedInitialData;

	public DevTestOneBlockEntity(BlockPos position, BlockState state) {
		//BlockEntityType<?> type,
		super(ArtsAndCraftsMod.DEV_TEST_1_BLOCK_ENTITY_TYPE.get(), position, state);
		ArtsAndCraftsMod.LOGGER.info("DevTestOneBlockEntity instantiated !");

		this.lastClientSpontaneousDataRequest = (long) (Math.random() * 80 + 1);
		this.hasClientReceivedInitialData = false;
	}

	//@Nullable
	//@Override
	//public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
	//	return null;
	//}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		ArtsAndCraftsMod.LOGGER.info("TestZeroBlockEntity.saveAdditional() called !");

		nbt.putString("test", "123");
		nbt.putString("given_item_id", ForgeRegistries.ITEMS.getKey(givenItemStack.getItem()).toString());
		super.saveAdditional(nbt);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, DevTestOneBlockEntity blockEntity) {
		/*if(level.isClientSide()) {
			if(!blockEntity.hasClientReceivedInitialData) {
				if(blockEntity.lastClientSpontaneousDataRequest > 0) {
					blockEntity.lastClientSpontaneousDataRequest--;
				} else if(blockEntity.lastClientSpontaneousDataRequest == 0) {
					// We send it and reset the countdown.
					blockEntity.lastClientSpontaneousDataRequest = RECURRENT_SPONTANEOUS_DATA_REQUESTS_DELAY;
					ModMessages.sendToServer(new TestZeroSyncC2SPacket(blockPos));
				}
			}
			return;
		}*/
	}

	public void setGivenItemStack(ItemStack stack) {
		ArtsAndCraftsMod.LOGGER.info("TestZeroBlockEntity.setGivenItemStack() called !");

		ArtsAndCraftsMod.LOGGER.info("givenItemStack was: " + ForgeRegistries.ITEMS.getKey(givenItemStack.getItem()).toString());

		this.givenItemStack = stack.copy();
		this.givenItemStack.setCount(1);

		ResourceLocation itemIdentifier = ForgeRegistries.ITEMS.getKey(givenItemStack.getItem());

		ArtsAndCraftsMod.LOGGER.info("givenItemStack is now: " + itemIdentifier.toString());

		setChanged();
	}

	@Override
	public void load(CompoundTag nbt) {
		ArtsAndCraftsMod.LOGGER.info("TestZeroBlockEntity.load() called !");

		if (nbt.getAllKeys().contains("given_item_id")) {
			this.givenItemStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("given_item_id"))).getDefaultInstance();
		}
		super.load(nbt);
	}

	public ItemStack getRenderedItemStack() {
		return this.givenItemStack;
	}

	public void handleSetPacket(ItemStack newItemStack) {
		ArtsAndCraftsMod.LOGGER.info("TestZeroBlockEntity.handleSetPacket() called !");
		this.givenItemStack = newItemStack;
		this.hasClientReceivedInitialData = true;
	}

	public void handleServerChange() {
		ArtsAndCraftsMod.LOGGER.info("TestZeroBlockEntity.handleServerChange() called !");
		//if (!level.isClientSide()) {
		//	ModMessages.sendToClients(new TestZeroSyncS2CPacket(this.givenItemStack, worldPosition));
		//}
	}
}