package net.darkhax.eplus.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, "eplus");

    public static final DeferredHolder<Block, Block> ADVANCED_TABLE = BLOCKS.register("advanced_table", BlockAdvancedTable::new);
    public static final DeferredHolder<Block, Block> DECORATIVE_BOOK = BLOCKS.register("decorative_book", BlockBookDecoration::new);
}
