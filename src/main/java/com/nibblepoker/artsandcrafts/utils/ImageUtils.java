package com.nibblepoker.artsandcrafts.utils;

import com.mojang.blaze3d.platform.NativeImage;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

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

    public static byte[] nativeImageToBytes(NativeImage nativeImage) {
        byte[] imageData = new byte[nativeImage.getWidth() * nativeImage.getHeight() * 4];

        for(int i = 0; i < imageData.length / 4; i++) {
            // We get a ABGR pixel from the image and swap to a ARGB pixel.
            // Gotta love the misleading function signature...
            int pixelARGB = ScreenUtils.swapRGB(nativeImage.getPixelRGBA(
                    i % nativeImage.getWidth(),
                    (int) Math.floor((double) i/ (double) nativeImage.getWidth())
            ));
            imageData[(i * 4)] = (byte) FastColor.ARGB32.red(pixelARGB);
            imageData[(i * 4) + 1] = (byte) FastColor.ARGB32.green(pixelARGB);
            imageData[(i * 4) + 2] = (byte) FastColor.ARGB32.blue(pixelARGB);
            imageData[(i * 4) + 3] = (byte) FastColor.ARGB32.alpha(pixelARGB);
        }

        return imageData;
    }

    public static NativeImage bytesToNativeImage(int width, int height, byte[] imageData) {
        NativeImage image = new NativeImage(NativeImage.Format.RGBA, width, height, true);

        for(int i = 0; i < (width * height) / 4; i++) {
            // We prepare an ARGB pixel.
            int pixelARGB = FastColor.ARGB32.color(
                    ((int) 0) | (imageData[(i * 4) + 3]),
                    ((int) 0) | (imageData[(i * 4)]),
                    ((int) 0) | (imageData[(i * 4) + 1]),
                    ((int) 0) | (imageData[(i * 4) + 2])
            );

            // We now apply that pixel as an ABGR pixel.
            image.setPixelRGBA(
                    i % width,
                    (int) Math.floor((double) i/ (double) width),
                    ScreenUtils.swapRGB(pixelARGB)
            );
        }

        return image;
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
