package net.darkhax.eplus.item;

import net.darkhax.eplus.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("eplus");

    public static final DeferredItem<BlockItem> ADVANCED_TABLE = ITEMS.registerSimpleBlockItem(ModBlocks.ADVANCED_TABLE);
    public static final DeferredItem<ItemTableUpgrade> TABLE_UPGRADE =
            ITEMS.registerItem("table_upgrade", ItemTableUpgrade::new, p -> p.stacksTo(16));
    public static final DeferredItem<ItemDecorativeBook> DECORATIVE_BOOK =
            ITEMS.registerItem("decorative_book", props -> new ItemDecorativeBook(ModBlocks.DECORATIVE_BOOK.get(), props));
}
