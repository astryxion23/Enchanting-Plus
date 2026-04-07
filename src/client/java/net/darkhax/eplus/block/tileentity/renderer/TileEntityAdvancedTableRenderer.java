package net.darkhax.eplus.block.tileentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TileEntityAdvancedTableRenderer extends TileEntityBookRenderer<TileEntityAdvancedTable> {

    private static final Identifier TEXTURE_BOOK = Identifier.fromNamespaceAndPath("eplus", "textures/entity/enchantingplus_book.png");

    private final ItemModelResolver itemModelResolver;

    public TileEntityAdvancedTableRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    protected Identifier getBookTexture(TileEntityAdvancedTable tile) {
        return TEXTURE_BOOK;
    }

    @Override
    protected float getHeightOffset(TileEntityAdvancedTable tile) {
        return 0.75f;
    }

    @Override
    public void extractRenderState(TileEntityAdvancedTable tile, EPlusBookRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(tile, state, partialTick, cameraPos, crumblingOverlay);
        ItemStack stack = tile.getDisplayStack();
        if (stack.isEmpty()) {
            state.hasFloatingItem = false;
            return;
        }
        float openness = Mth.lerp(partialTick, tile.bookSpreadPrev, tile.bookSpread);
        if (openness <= 0.001F) {
            state.hasFloatingItem = false;
            return;
        }
        state.hasFloatingItem = true;
        state.floatingItemOpenness = openness;
        this.itemModelResolver.updateForTopItem(state.floatingItemState, stack, ItemDisplayContext.GROUND, tile.getLevel(), null, 0);
    }

    @Override
    public void submit(EPlusBookRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        if (!state.hasFloatingItem) return;
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.0D, 0.5D);
        float time = state.time;
        float hover = Mth.sin(time / 10.0F) * 0.1F + 0.1F;
        float modelYScale = (float) state.floatingItemState.getModelBoundingBox().getYsize();
        float openness = state.floatingItemOpenness;
        poseStack.translate(0.0D, hover + 0.25F * modelYScale * openness - 0.15F * (1.0F - openness), 0.0D);
        float scale = openness * 0.8F + 0.2F;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotation(time / 20.0F));
        state.floatingItemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
