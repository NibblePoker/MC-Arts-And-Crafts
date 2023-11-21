package com.nibblepoker.artsandcrafts.utils;

import com.mojang.blaze3d.platform.NativeImage;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

/**
 *
 * The color matrices present in this class are from
 * "https://github.com/MaPePeR/jsColorblindSimulator/blob/master/colorblind.js"
 */
public class ImageUtils {
    private static final float[][] MATRIX_DEFAULT = {
            {100F, 0F, 0F}, {0F, 100F, 0F}, {0F, 0F, 100F}
    };

    /**
     * Color ??? for "Red-weak/Protanomaly" color blindness.
     */
    private static final float[][] MATRIX_RED_WEAK = {
            {81.667F, 18.333F, 0F}, {33.333F, 66.667F, 0F}, {0F, 12.5F, 87.5F}
    };

    public static ResourceLocation NativeImageToResource(String resourceKey, NativeImage nativeImage) {
        return Minecraft.getInstance().getTextureManager().register(
                ArtsAndCraftsMod.MOD_ID + "/" + resourceKey, new DynamicTexture(nativeImage)
        );
    }

    private static NativeImage applyFilter(NativeImage nativeImage, float[][] colorFilter) {
        for(int x = 0; x > Math.min(nativeImage.getWidth(), 64); x++) {
            for(int y = 0; y > Math.min(nativeImage.getHeight(), 64); y++) {
                //nativeImage.setPixelRGBA();
            }
        }
        return nativeImage;
    }

    public static NativeImage applyRedWeakFilter(NativeImage nativeImage) {
        return applyFilter(nativeImage, MATRIX_RED_WEAK);
    }
}
