package net.darkhax.eplus.item;

import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {

    public static Item ADVANCED_TABLE;
    public static Item TABLE_UPGRADE;
    public static Item DECORATIVE_BOOK;

    public static void register() {
        ADVANCED_TABLE = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("eplus", "advanced_table"),
                new BlockItem(ModBlocks.ADVANCED_TABLE, new Item.Properties()));
        TABLE_UPGRADE = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("eplus", "table_upgrade"),
                new ItemTableUpgrade(new Item.Properties().stacksTo(16)));
        DECORATIVE_BOOK = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("eplus", "decorative_book"),
                new ItemDecorativeBook(ModBlocks.DECORATIVE_BOOK, new Item.Properties()));
    }
}
