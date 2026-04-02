package net.darkhax.eplus.item;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "eplus");

    public static final RegistryObject<Item> ADVANCED_TABLE = ITEMS.register("advanced_table",
            () -> new BlockItem(ModBlocks.ADVANCED_TABLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TABLE_UPGRADE = ITEMS.register("table_upgrade",
            () -> new ItemTableUpgrade(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> DECORATIVE_BOOK = ITEMS.register("decorative_book",
            () -> new ItemDecorativeBook(ModBlocks.DECORATIVE_BOOK.get(), new Item.Properties()));
}
