package net.darkhax.eplus.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ModBlocks {

    public static final Block ADVANCED_TABLE = Registry.register(BuiltInRegistries.BLOCK,
            new ResourceLocation("eplus", "advanced_table"), new BlockAdvancedTable());
    public static final Block DECORATIVE_BOOK = Registry.register(BuiltInRegistries.BLOCK,
            new ResourceLocation("eplus", "decorative_book"), new BlockBookDecoration());
}
