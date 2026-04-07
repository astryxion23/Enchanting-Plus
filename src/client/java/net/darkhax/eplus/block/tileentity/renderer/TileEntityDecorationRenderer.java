package net.darkhax.eplus.block.tileentity.renderer;

import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;

public class TileEntityDecorationRenderer extends TileEntityBookRenderer<TileEntityDecoration> {

    private static final Identifier[] BOOK_TEXTURES = new Identifier[] {
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/enchantingplus_book.png"),
            Identifier.fromNamespaceAndPath("minecraft", "textures/entity/enchantment/enchanting_table_book.png"),
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/prismarine_book.png"),
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/nether_book.png"),
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/tartarite_book.png"),
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/white_book.png"),
            Identifier.fromNamespaceAndPath("eplus", "textures/entity/metal_book.png")
    };

    public TileEntityDecorationRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getBookTexture(TileEntityDecoration tile) {
        int meta = tile.variant;
        return meta >= 0 && meta < BOOK_TEXTURES.length ? BOOK_TEXTURES[meta] : BOOK_TEXTURES[0];
    }

    @Override
    protected float getHeightOffset(TileEntityDecoration tile) {
        return 0.75f + tile.height;
    }
}
