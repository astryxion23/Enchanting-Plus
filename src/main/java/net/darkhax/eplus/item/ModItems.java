package net.darkhax.eplus.item;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "eplus");

    public static final DeferredHolder<Item, Item> ADVANCED_TABLE = ITEMS.register("advanced_table",
            () -> new BlockItem(ModBlocks.ADVANCED_TABLE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> TABLE_UPGRADE = ITEMS.register("table_upgrade",
            () -> new ItemTableUpgrade(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> DECORATIVE_BOOK = ITEMS.register("decorative_book",
            () -> new ItemDecorativeBook(ModBlocks.DECORATIVE_BOOK.get(), new Item.Properties()));
}
