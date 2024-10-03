package cy.jdkdigital.cobblegengalore.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cy.jdkdigital.cobblegengalore.common.block.entity.BlockGenBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nonnull;

public class BlockGenBlockEntityRenderer implements BlockEntityRenderer<BlockGenBlockEntity>
{
    public BlockGenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(BlockGenBlockEntity blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        if (blockEntity.hasResult()) {
            if (blockEntity.getResultBlock() != null) {
                poseStack.pushPose();
                poseStack.translate(0.15f, 0.15f, 0.15f);
                poseStack.scale(0.7f, 0.7f, 0.7f);
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockEntity.getResultBlock(), poseStack, bufferSource, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, RenderType.SOLID);
                poseStack.popPose();
            } else if (blockEntity.getResultItem() != null) {
                double tick = System.currentTimeMillis() / 800.0D;
                poseStack.pushPose();
                poseStack.translate(0.5f, 0.5f, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees((float) ((tick * 30.0D) % 360)));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                Minecraft.getInstance().getItemRenderer().renderStatic(blockEntity.getResultItem(), ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, poseStack, bufferSource, blockEntity.getLevel(), 0);
                poseStack.popPose();
            }
        }
    }
}