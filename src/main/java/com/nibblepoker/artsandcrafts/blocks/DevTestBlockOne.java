package com.nibblepoker.artsandcrafts.blocks;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.blocks.entities.DevTestOneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DevTestBlockOne extends BaseEntityBlock {
	public DevTestBlockOne(Properties blockProperties) {
		super(blockProperties);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DevTestOneBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
		return createTickerHelper(entityType, ArtsAndCraftsMod.DEV_TEST_1_BLOCK_ENTITY_TYPE.get(), DevTestOneBlockEntity::tick);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.getBlock() != pNewState.getBlock()) {
			BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
			/*if (blockEntity instanceof DevTestOneBlockEntity) {
				((DevTestOneBlockEntity) blockEntity).drops();
			}*/
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		// Only doing stuff on the server.
		// Any changes to the block entity should get propagated to the client via the block entity's class.
		if(!pLevel.isClientSide()) {
			BlockEntity entity = pLevel.getBlockEntity(pPos);

			if(entity == null) {
				ArtsAndCraftsMod.LOGGER.error("Missing tile entity !");
			} else {
				if(entity instanceof DevTestOneBlockEntity) {
					ArtsAndCraftsMod.LOGGER.info("All good :)");

					//((DevTestOneBlockEntity) entity).setGivenItemStack(player.getMainHandItem());
					((DevTestOneBlockEntity) entity).handleServerChange();

					// Cannot be done on server !
					//playDebugSound(player);
				} else {
					ArtsAndCraftsMod.LOGGER.error("Invalid type for block entity, reeeeeee !");
				}
			}
		}

		if (!pLevel.isClientSide()) {
			BlockEntity entity = pLevel.getBlockEntity(pPos);
			/*if(entity instanceof DevTestOneBlockEntity) {
				NetworkHooks.openScreen(((ServerPlayer)pPlayer), (DevTestOneBlockEntity)entity, pPos);
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}*/
		}
		
		return InteractionResult.sidedSuccess(pLevel.isClientSide());
	}
}
