package net.darkhax.eplus.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static Block ADVANCED_TABLE;
    public static Block DECORATIVE_BOOK;

    public static void register() {
        ADVANCED_TABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("eplus", "advanced_table"), new BlockAdvancedTable());
        DECORATIVE_BOOK = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath("eplus", "decorative_book"), new BlockBookDecoration());
    }
}
