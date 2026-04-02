package net.darkhax.eplus.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

/**
 * BlockItem that cannot be placed in the world. Used for the decorative book
 * so it appears in the creative tab but does not place a block on use.
 */
public class ItemDecorativeBook extends BlockItem {

    public ItemDecorativeBook(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.FAIL; // prevent placing the block
    }
}
