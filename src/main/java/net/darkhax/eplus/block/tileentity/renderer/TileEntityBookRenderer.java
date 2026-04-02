package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.darkhax.eplus.block.tileentity.TileEntityWithBook;

public abstract class TileEntityBookRenderer<T extends TileEntityWithBook> extends TileEntityRenderer<T> {

    private static final BookModel BOOK_MODEL = new BookModel();

    public TileEntityBookRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T te, float partialTicks, MatrixStack m, IRenderTypeBuffer buf, int light, int overlay) {
        m.pushPose();
        m.translate(0.5D, getHeightOffset(te), 0.5D);
        // Vanilla-style bobbing
        float time = te.tickCount + partialTicks;
        m.translate(0.0D, 0.1D + MathHelper.sin(time * 0.1F) * 0.01F, 0.0D);
        // Rotation with normalized delta (smooth lerp)
        float delta = te.bookRotation - te.bookRotationPrev;
        while (delta >= (float) Math.PI) delta -= (float) Math.PI * 2F;
        while (delta < -(float) Math.PI) delta += (float) Math.PI * 2F;
        float rotation = te.bookRotationPrev + delta * partialTicks;
        m.mulPose(Vector3f.YP.rotation(-rotation));
        m.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
        // Page flip mapping (vanilla style)
        float flip = MathHelper.lerp(partialTicks, te.pageFlipPrev, te.pageFlip);
        float left = MathHelper.frac(flip + 0.25F) * 1.6F - 0.3F;
        float right = MathHelper.frac(flip + 0.75F) * 1.6F - 0.3F;
        left = MathHelper.clamp(left, 0.0F, 1.0F);
        right = MathHelper.clamp(right, 0.0F, 1.0F);
        float open = MathHelper.lerp(partialTicks, te.bookSpreadPrev, te.bookSpread);
        BOOK_MODEL.setupAnim(time, left, right, open);
        RenderMaterial material = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, getTexture(te));
        IVertexBuilder builder = material.buffer(buf, RenderType::entityCutoutNoCull);
        BOOK_MODEL.renderToBuffer(m, builder, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        m.popPose();
    }

    abstract ResourceLocation getTexture(T tile);
    abstract float getHeightOffset(T tile);
}
