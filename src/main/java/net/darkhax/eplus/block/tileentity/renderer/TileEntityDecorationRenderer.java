package net.darkhax.eplus.block.tileentity.renderer;

import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class TileEntityDecorationRenderer extends TileEntityBookRenderer<TileEntityDecoration> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation("eplus", "entity/enchantingplus_book"),
            new ResourceLocation("minecraft", "entity/enchanting_table_book"),
            new ResourceLocation("eplus", "entity/prismarine_book"),
            new ResourceLocation("eplus", "entity/nether_book"),
            new ResourceLocation("eplus", "entity/tartarite_book"),
            new ResourceLocation("eplus", "entity/white_book"),
            new ResourceLocation("eplus", "entity/metal_book")
    };

    public TileEntityDecorationRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
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
