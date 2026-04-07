package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class TileEntityBookRenderer<T extends TileEntityWithBook> implements BlockEntityRenderer<T, EPlusBookRenderState> {

    private final BookModel bookModel;

    protected TileEntityBookRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    protected abstract Identifier getBookTexture(T tile);

    protected abstract float getHeightOffset(T tile);

    @Override
    public EPlusBookRenderState createRenderState() {
        return new EPlusBookRenderState();
    }

    @Override
    public void extractRenderState(T te, EPlusBookRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(te, state, partialTick, cameraPos, crumblingOverlay);
        state.heightOffset = this.getHeightOffset(te);
        state.flip = Mth.lerp(partialTick, te.pageFlipPrev, te.pageFlip);
        state.open = Mth.lerp(partialTick, te.bookSpreadPrev, te.bookSpread);
        state.time = te.tickCount + partialTick;
        float delta = te.bookRotation - te.bookRotationPrev;
        while (delta >= (float) Math.PI) delta -= (float) Math.PI * 2F;
        while (delta < -(float) Math.PI) delta += (float) Math.PI * 2F;
        state.yRot = te.bookRotationPrev + delta * partialTick;
        state.bookTexture = this.getBookTexture(te);
    }

    @Override
    public void submit(EPlusBookRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5D, state.heightOffset, 0.5D);
        poseStack.translate(0.0D, 0.1D + Mth.sin(state.time * 0.1F) * 0.01F, 0.0D);
        poseStack.mulPose(Axis.YP.rotation(-state.yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float left = Mth.frac(state.flip + 0.25F) * 1.6F - 0.3F;
        float right = Mth.frac(state.flip + 0.75F) * 1.6F - 0.3F;
        left = Mth.clamp(left, 0.0F, 1.0F);
        right = Mth.clamp(right, 0.0F, 1.0F);
        BookModel.State bookState = BookModel.State.forAnimation(state.time, left, right, state.open);
        submitNodeCollector.submitModel(
                this.bookModel,
                bookState,
                poseStack,
                state.bookTexture,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0,
                state.breakProgress
        );
        poseStack.popPose();
    }
}
