package net.darkhax.eplus.block.tileentity.renderer;

import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TileEntityDecorationRenderer extends TileEntityBookRenderer<TileEntityDecoration> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation("eplus", "textures/entity/enchantingplus_book.png"),
            new ResourceLocation("minecraft", "textures/entity/enchanting_table_book.png"),
            new ResourceLocation("eplus", "textures/entity/prismarine_book.png"),
            new ResourceLocation("eplus", "textures/entity/nether_book.png"),
            new ResourceLocation("eplus", "textures/entity/tartarite_book.png"),
            new ResourceLocation("eplus", "textures/entity/white_book.png"),
            new ResourceLocation("eplus", "textures/entity/metal_book.png")
    };

    public TileEntityDecorationRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    ResourceLocation getTexture(TileEntityDecoration tile) {
        int meta = tile.variant;
        return meta >= 0 && meta < TEXTURES.length ? TEXTURES[meta] : TEXTURES[0];
    }

    @Override
    float getHeightOffset(TileEntityDecoration tile) {
        return 0.75f + tile.height;
    }
}
