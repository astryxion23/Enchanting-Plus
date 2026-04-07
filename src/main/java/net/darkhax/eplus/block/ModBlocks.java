package net.darkhax.eplus.block;

import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("eplus");

    public static final DeferredBlock<BlockAdvancedTable> ADVANCED_TABLE =
            BLOCKS.registerBlock("advanced_table", BlockAdvancedTable::new,
                    p -> p.mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 2000.0F)
                            .noOcclusion());

    public static final DeferredBlock<BlockBookDecoration> DECORATIVE_BOOK =
            BLOCKS.registerBlock("decorative_book", BlockBookDecoration::new,
                    p -> p.strength(1.5F).lightLevel(s -> 15).noOcclusion());
}
