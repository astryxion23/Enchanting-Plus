package net.darkhax.eplus.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

/**
 * BlockItem that cannot be placed in the world. Used for the decorative book
 * so it appears in the creative tab but does not place a block on use.
 */
public class ItemDecorativeBook extends BlockItem {

    public ItemDecorativeBook(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        return ActionResultType.FAIL; // prevent placing the block
    }
}
