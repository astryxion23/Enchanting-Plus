package net.darkhax.eplus.item;

import net.darkhax.eplus.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final ResourceKey<Item> ADVANCED_TABLE_KEY =
            ResourceKey.create(Registries.ITEM, ModBlocks.ADVANCED_TABLE_KEY.identifier());
    public static final ResourceKey<Item> TABLE_UPGRADE_KEY =
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("eplus", "table_upgrade"));
    public static final ResourceKey<Item> DECORATIVE_BOOK_KEY =
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("eplus", "decorative_book"));

    public static final BlockItem ADVANCED_TABLE = registerAdvancedTable();
    public static final ItemTableUpgrade TABLE_UPGRADE = registerTableUpgrade();
    public static final ItemDecorativeBook DECORATIVE_BOOK = registerDecorativeBook();

    private static BlockItem registerAdvancedTable() {
        BlockItem item = new BlockItem(ModBlocks.ADVANCED_TABLE, new Item.Properties().setId(ADVANCED_TABLE_KEY));
        return Registry.register(BuiltInRegistries.ITEM, ADVANCED_TABLE_KEY, item);
    }

    private static ItemTableUpgrade registerTableUpgrade() {
        ItemTableUpgrade item = new ItemTableUpgrade(new Item.Properties().stacksTo(16).setId(TABLE_UPGRADE_KEY));
        return Registry.register(BuiltInRegistries.ITEM, TABLE_UPGRADE_KEY, item);
    }

    private static ItemDecorativeBook registerDecorativeBook() {
        ItemDecorativeBook item = new ItemDecorativeBook(ModBlocks.DECORATIVE_BOOK,
                new Item.Properties().setId(DECORATIVE_BOOK_KEY));
        return Registry.register(BuiltInRegistries.ITEM, DECORATIVE_BOOK_KEY, item);
    }
}
