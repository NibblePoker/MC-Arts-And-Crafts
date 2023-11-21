package com.nibblepoker.artsandcrafts.blocks.renderers;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.blocks.entities.DevTestOneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DevTestOneBlockRenderer implements BlockEntityRenderer<DevTestOneBlockEntity> {
    private final ItemStack internalItemStack;

    private final NativeImage nativeImage;
    //private final SimpleTexture simpleTexture;

    public DevTestOneBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.internalItemStack = new ItemStack(ArtsAndCraftsMod.NEON_TUBE_YELLOW.get());

        this.nativeImage = new NativeImage(NativeImage.Format.RGBA, 16, 16, true);

        // x, y, width, height
        // As Alpha, Green, Blue, Red
        this.nativeImage.fillRect(1, 1, 9, 9, 0xFF7F00FF);
        this.nativeImage.fillRect(6, 6, 9, 9, 0xFF00FF7F);

        //this.simpleTexture = SimpleTexture.do(null, this.nativeImage);
        // Test Export
        //try {
        //    this.nativeImage.writeToFile(Paths.get("D:\\DevelopmentNew\\Minecraft\\arts-and-crafts\\reee.png"));
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
    }

    //Copy of SimpleTexture.doLoad
    //private void doLoad(NativeImage p_118137_, boolean p_118138_, boolean p_118139_) {
    //    TextureUtil.prepareImage(this.getId(), 0, p_118137_.getWidth(), p_118137_.getHeight());
    //    p_118137_.upload(0, 0, 0, 0, 0, p_118137_.getWidth(), p_118137_.getHeight(), p_118138_, p_118139_, false, true);
    //}

    // There shouldn't be a need to manually free the nativeimage, the close() function is called instead.
    // See AutoClosable in deprecation notice !
    //@Override
    //protected void finalize() throws Throwable {
    //    // Cleanup code here
    //}

    public ResourceLocation getTextureLocation() {
        return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlasLocation();
    }

    @Override
    public void render(DevTestOneBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        //ItemStack itemStack = blockEntity.getRenderedItemStack();

        //VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.debugQuads());
        //VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(this.getTextureLocation()));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.endPortal());

        //ArtsAndCraftsMod.LOGGER.info("Rendering: "+this.internalItemStack);

        //Matrix4f matrix4f = posestack$pose.pose();
        //Matrix3f matrix3f = posestack$pose.normal();

        poseStack.pushPose();


        PoseStack.Pose poseStackPose = poseStack.last();
        Matrix4f matrix4f = poseStackPose.pose();
        Matrix3f matrix3f = poseStackPose.normal();

        //ArtsAndCraftsMod.LOGGER.info("Normal: "+poseStackPose.normal());
        //ArtsAndCraftsMod.LOGGER.info("Pose: "+poseStackPose.pose());

        int l1 = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos());

        poseStack.translate(0.5F, 1.25F, 0.5F);

        this.vertex(matrix4f, matrix3f, vertexconsumer, 0.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0, 0, -1, l1);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -0.5F, -0.5F, 0.0F, 1.0F, 0.0F, 0, 0, 1, l1);
        this.vertex(matrix4f, matrix3f, vertexconsumer, -0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0, 0, 1, l1);
        this.vertex(matrix4f, matrix3f, vertexconsumer, 0.5F, 0.5F, 0.0F, 0.0F, 1.0F, 0, 0, 1, l1);

        //poseStack.scale(0.25F, 0.25F, 0.25F);
        //poseStack.
        //poseStack.mulPose(Quaternionf.);

        //public void renderStatic(ItemStack p_270761_, ItemDisplayContext p_270648_,
        // int p_270410_, int p_270894_, PoseStack p_270430_,
        // MultiBufferSource p_270457_, @Nullable Level p_270149_, int p_270509_) {

        //this.itemRenderer.renderStatic(itemstack,
        // ItemDisplayContext.FIXED, k,
        // OverlayTexture.NO_OVERLAY, p_115079_, p_115080_, p_115076_.level(), p_115076_.getId());

        itemRenderer.renderStatic(
                this.internalItemStack,
                ItemDisplayContext.GROUND,
                getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos().above()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0
        );
        //packedOverlay
        //blockEntity.i()

        //itemRenderer.renderStatic(
        //        this.internalItemStack,
        //        ItemDisplayContext.GROUND,
        //        0,
        //        //ItemTransforms.TransformType.GROUND, // 1.19
        //        ItemTransforms.NO_TRANSFORMS.ground,
        //        poseStack,
        //        getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos().above()),
        //        //packedLight,
        //        OverlayTexture.NO_OVERLAY, poseStack, bufferSource, 1);

        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer,
                               float x, float y, float z, float u, float v,
                               float normalX, float normalY, float normalZ, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z - 0.5f)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, normalX, normalY, normalZ)
                .endVertex();
    }
}

