package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GlueItem extends Item {
    private static final int DRINK_DURATION = 64;

    public GlueItem(Properties itemProperties) {
        super(itemProperties);
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, Level level,
                                              @NotNull LivingEntity usingEntity) {
        // Giving the player Poison for 5 seconds & confusion for 10 seconds.
        if(!level.isClientSide) {
            usingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            usingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }

        if (usingEntity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (usingEntity instanceof Player && !((Player)usingEntity).getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return itemStack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : itemStack;
    }

    public int getUseDuration(@NotNull ItemStack itemStack) {
        return DRINK_DURATION;
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    public @NotNull SoundEvent getDrinkingSound() {
        return ArtsAndCraftsMod.GLUE_SLURP_SOUND.get();
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
