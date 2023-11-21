package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NeonTubeItem extends Item {
    private final Item droppedItem;

    public NeonTubeItem(Item droppedItem, Properties itemProperties) {
        super(itemProperties);
        this.droppedItem = droppedItem;
    }

    /** Handles hits against entities & breaks the item on the first hit. */
    @Override
    public boolean hurtEnemy(@NotNull ItemStack usedItem, LivingEntity targetEntity,@NotNull LivingEntity sourceEntity) {
        targetEntity.playSound(ArtsAndCraftsMod.NEON_BREAKING_SOUND.get(), 0.45F, 1.0F);

        // We only reduce the stack and spawn broken tubes if we're not in creative.
        if((sourceEntity instanceof Player) && !((Player)sourceEntity).isCreative()) {
            usedItem.shrink(1);

            // Dropping a "broken neon tube" at the attacker and target's feet.
            sourceEntity.level().addFreshEntity(new ItemEntity(
                    sourceEntity.level(),
                    sourceEntity.position().x, sourceEntity.position().y, sourceEntity.position().z,
                    new ItemStack(droppedItem, 1)
            ));
            targetEntity.level().addFreshEntity(new ItemEntity(
                    targetEntity.level(),
                    targetEntity.position().x, targetEntity.position().y, targetEntity.position().z,
                    new ItemStack(droppedItem, 1)
            ));

            // Spawning a short-lived lingering potion effect in between the entities to simulate
            //  the toxic gas present in those tubes.
            Vec3 potionPos = targetEntity.position();

            AreaEffectCloud poisonCloud = new AreaEffectCloud(sourceEntity.level(), potionPos.x, potionPos.y, potionPos.z);
            poisonCloud.setOwner(sourceEntity);
            poisonCloud.setRadius(1.5F);
            poisonCloud.setRadiusOnUse(-0.5F);
            poisonCloud.setWaitTime(5);
            poisonCloud.setRadiusPerTick(-poisonCloud.getRadius() / (float)poisonCloud.getDuration());
            poisonCloud.setPotion(Potions.POISON);
            poisonCloud.setFixedColor(0xFF4E9331);
            targetEntity.level().addFreshEntity(poisonCloud);
        }

        return true;
    }
}
