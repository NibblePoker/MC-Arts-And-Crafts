package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.DesignerTabScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DesignerTabItem extends Item {
    public DesignerTabItem(Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
        // Playing a "page-flipping" sound.
        user.playSound(SoundEvents.VILLAGER_WORK_LIBRARIAN, 0.8F, 0.8F + user.level().getRandom().nextFloat() * 0.4F);

        // TODO: Showing the client-side GUI for ???
        if(world.isClientSide()) {
            ArtsAndCraftsMod.LOGGER.debug("Showing Designer Tab's Screen...");
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
        }

        return InteractionResultHolder.fail(user.getItemInHand(hand));
    }

    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip." + ArtsAndCraftsMod.MOD_ID + "test"));
    }
}
