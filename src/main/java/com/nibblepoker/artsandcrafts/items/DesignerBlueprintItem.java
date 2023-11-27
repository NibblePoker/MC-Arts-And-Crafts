package com.nibblepoker.artsandcrafts.items;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.DesignerTabScreen;
import com.nibblepoker.artsandcrafts.interfaces.ImagePreviewScreen;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.EArtFormat;
import com.nibblepoker.artsandcrafts.logic.managers.ArtManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DesignerBlueprintItem extends Item {
    public static final String NBT_IMAGE_HASH_KEY = "hash";

    public DesignerBlueprintItem(Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        // Retrieving the relevant image's hash.
        ItemStack usedStack = player.getItemInHand(hand);
        String imageHash = null;
        CompoundTag stackNbtCompound = usedStack.getTag();
        if (usedStack.hasTag() && stackNbtCompound != null) {
            if(stackNbtCompound.contains(NBT_IMAGE_HASH_KEY, CompoundTag.TAG_STRING)) {
                imageHash = stackNbtCompound.getString(NBT_IMAGE_HASH_KEY);
            }
        }
        if(imageHash == null || !ArtManager.isSHA1Hash(imageHash)) {
            if(world.isClientSide()) {
                ArtsAndCraftsMod.LOGGER.error("Used a 'DesignerBlueprintItem' that doesn't have the '" +
                        NBT_IMAGE_HASH_KEY + "' NBT tag !");
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.00F));
            }
            player.getCooldowns().addCooldown(this, 20);
            usedStack.setPopTime(5);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        // We have a "valid" SHA1 hash, we can now attempt to load the image data.

        // FIXME: Remove this later !
        ArtsAndCraftsMod.artManager.doDebugPrintout();
        // FIXME: The new art isn't added when newly made !

        // Checking if we have the image in the local cache
        // If we don't have it, we send a message to the server for it to send it to us.
        // This shouldn't happen since we ask for that data when loading or picking up the item.
        if(!ArtsAndCraftsMod.artManager.hasImage(imageHash)) {
            if(world.isClientSide()) {
                ArtsAndCraftsMod.LOGGER.error("We don't have a local copy of the image '" + imageHash + "', we'll ask the server...");
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.00F));
            }
            player.getCooldowns().addCooldown(this, 20);
            usedStack.setPopTime(5);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        // We have a temporary copy of the desired image.
        // FIXME: We get bad data on valid shit stuff !
        ArtData shownArtData = ArtsAndCraftsMod.artManager.getArtDataCopy(imageHash);
        if(shownArtData == null || EArtFormat.getFromCode(shownArtData.getImageFormat()).isManagementFormat()) {
            if(world.isClientSide()) {
                ArtsAndCraftsMod.LOGGER.error("The retrieved art data for '" + imageHash + "' is invalid !");
                ArtsAndCraftsMod.LOGGER.debug(String.valueOf(shownArtData));
                if(shownArtData != null) {
                    ArtsAndCraftsMod.LOGGER.debug(EArtFormat.getFromCode(shownArtData.getImageFormat()).toString());
                }
                ArtsAndCraftsMod.LOGGER.debug(String.valueOf(shownArtData));
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.00F));
            }
            player.getCooldowns().addCooldown(this, 20);
            usedStack.setPopTime(5);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        // We show the image viewer.
        player.playSound(SoundEvents.VILLAGER_WORK_LIBRARIAN, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
        if(world.isClientSide()) {
            ArtsAndCraftsMod.LOGGER.debug("Showing the image viewer GUI for '" + imageHash + "'...");
            Minecraft.getInstance().setScreen(new ImagePreviewScreen(shownArtData, false, true));
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));

        // FIXME: Reduces operations on server !!!
    }

}