package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TileEntityAdvancedTableRenderer extends TileEntityBookRenderer<TileEntityAdvancedTable> {

    private static final ResourceLocation TEXTURE_BOOK = ResourceLocation.fromNamespaceAndPath("eplus", "textures/entity/enchantingplus_book.png");

    public TileEntityAdvancedTableRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    ResourceLocation getTexture(TileEntityAdvancedTable tile) {
        return TEXTURE_BOOK;
    }

    @Override
    float getHeightOffset(TileEntityAdvancedTable tile) {
        return 0.75f;
    }

    @Override
    public void render(TileEntityAdvancedTable tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(tile, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        ItemStack stack = tile.getDisplayStack();
        if (stack.isEmpty()) return;
        float openness = Mth.lerp(partialTicks, tile.bookSpreadPrev, tile.bookSpread);
        if (openness <= 0.001F) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.0D, 0.5D);
        float time = tile.tickCount + partialTicks;
        float hover = Mth.sin(time / 10.0F) * 0.1F + 0.1F;
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, tile.getLevel(), null, 0);
        float modelYScale = model.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        matrixStack.translate(0.0D, hover + 0.25F * modelYScale * openness - 0.15F * (1.0F - openness), 0.0D);
        float scale = openness * 0.8F + 0.2F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Axis.YP.rotation(time / 20.0F));
        itemRenderer.render(stack, ItemDisplayContext.GROUND, false, matrixStack, buffer, combinedLight, combinedOverlay, model);
        matrixStack.popPose();
    }
}
