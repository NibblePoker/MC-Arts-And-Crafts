package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NeonTubeItem extends Item {
    public NeonTubeItem(Properties settings) {
        super(settings);
    }

    /** Handles hits against entities & breaks the item on the first hit. */
    @Override
    public boolean hurtEnemy(@NotNull ItemStack usedItem, LivingEntity targetEntity,@NotNull LivingEntity sourceEntity) {
        targetEntity.playSound(ArtsAndCraftsMod.NEON_BREAKING_SOUND.get(), 0.45F, 1.0F);

        // We only reduce the stack and spawn broken tubes if we're not in creative.
        if((sourceEntity instanceof Player) && !((Player)sourceEntity).isCreative()) {
            usedItem.shrink(1);

            sourceEntity.level().addFreshEntity(new ItemEntity(
                    sourceEntity.level(),
                    sourceEntity.position().x, sourceEntity.position().y, sourceEntity.position().z,
                    new ItemStack(ArtsAndCraftsMod.BROKEN_NEON_TUBE.get(), 1)
            ));

            targetEntity.level().addFreshEntity(new ItemEntity(
                    targetEntity.level(),
                    targetEntity.position().x, targetEntity.position().y, targetEntity.position().z,
                    new ItemStack(ArtsAndCraftsMod.BROKEN_NEON_TUBE.get(), 1)
            ));
        }

        return true;
    }
}
