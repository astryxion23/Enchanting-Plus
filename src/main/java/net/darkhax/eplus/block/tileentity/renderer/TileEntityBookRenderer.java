package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.darkhax.eplus.block.tileentity.TileEntityWithBook;

public abstract class TileEntityBookRenderer<T extends TileEntityWithBook> implements BlockEntityRenderer<T> {

    private final BookModel bookModel;

    public TileEntityBookRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(T te, float partialTicks, com.mojang.blaze3d.vertex.PoseStack m, MultiBufferSource buf, int light, int overlay) {
        m.pushPose();
        m.translate(0.5D, getHeightOffset(te), 0.5D);
        float time = te.tickCount + partialTicks;
        m.translate(0.0D, 0.1D + Mth.sin(time * 0.1F) * 0.01F, 0.0D);
        float delta = te.bookRotation - te.bookRotationPrev;
        while (delta >= (float) Math.PI) delta -= (float) Math.PI * 2F;
        while (delta < -(float) Math.PI) delta += (float) Math.PI * 2F;
        float rotation = te.bookRotationPrev + delta * partialTicks;
        m.mulPose(Axis.YP.rotation(-rotation));
        m.mulPose(Axis.ZP.rotationDegrees(80.0F));
        float flip = Mth.lerp(partialTicks, te.pageFlipPrev, te.pageFlip);
        float left = Mth.frac(flip + 0.25F) * 1.6F - 0.3F;
        float right = Mth.frac(flip + 0.75F) * 1.6F - 0.3F;
        left = Mth.clamp(left, 0.0F, 1.0F);
        right = Mth.clamp(right, 0.0F, 1.0F);
        float open = Mth.lerp(partialTicks, te.bookSpreadPrev, te.bookSpread);
        this.bookModel.setupAnim(time, left, right, open);
        ResourceLocation texture = getTexture(te);
        VertexConsumer vertexConsumer = buf.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.bookModel.renderToBuffer(m, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        m.popPose();
    }

    abstract ResourceLocation getTexture(T tile);
    abstract float getHeightOffset(T tile);
}
