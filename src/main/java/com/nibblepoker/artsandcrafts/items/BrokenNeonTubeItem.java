package com.nibblepoker.artsandcrafts.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BrokenNeonTubeItem extends SwordItem {
    public BrokenNeonTubeItem(Properties settings) {
        super(Tiers.WOOD, 1, -4.0F, settings);
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState targetBlock) {
        return false;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack usedItem, @NotNull BlockState brokenBlock) {
        return 0.1F;
    }

    public boolean mineBlock(@NotNull ItemStack usedItem, @NotNull Level level, BlockState blockState,
                             @NotNull BlockPos blockPos, @NotNull LivingEntity sourceEntity) {
        // Breaking the tube after mining a single block.
        if (blockState.getDestroySpeed(level, blockPos) != 0.0F) {
            usedItem.hurtAndBreak(99, sourceEntity, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
}
