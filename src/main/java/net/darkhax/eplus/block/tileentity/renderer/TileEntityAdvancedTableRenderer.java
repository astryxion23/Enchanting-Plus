package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class TileEntityAdvancedTableRenderer extends TileEntityBookRenderer<TileEntityAdvancedTable> {

    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("eplus", "entity/enchantingplus_book");

    public TileEntityAdvancedTableRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    ResourceLocation getTexture(TileEntityAdvancedTable tile) {
        return TEXTURE_BOOK;
    }

    @Override
    float getHeightOffset(TileEntityAdvancedTable tile) {
        return 0.75f; // Match Enchanting Infuser / vanilla enchanting table book position
    }

    @Override
    public void render(TileEntityAdvancedTable tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        super.render(tile, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        ItemStack stack = tile.getDisplayStack();
        if (stack.isEmpty()) return;
        float openness = MathHelper.lerp(partialTicks, tile.bookSpreadPrev, tile.bookSpread);
        if (openness <= 0.001F) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.0D, 0.5D);
        float time = tile.tickCount + partialTicks;
        float hover = MathHelper.sin(time / 10.0F) * 0.1F + 0.1F;
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, tile.getLevel(), null);
        float modelYScale = model.getTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y();
        matrixStack.translate(0.0D, hover + 0.25F * modelYScale * openness - 0.15F * (1.0F - openness), 0.0D);
        float scale = openness * 0.8F + 0.2F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Vector3f.YP.rotation(time / 20.0F));
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, model);
        matrixStack.popPose();
    }
}
