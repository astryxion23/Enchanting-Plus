package net.darkhax.eplus.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "eplus");

    public static final RegistryObject<Block> ADVANCED_TABLE = BLOCKS.register("advanced_table", BlockAdvancedTable::new);
    public static final RegistryObject<Block> DECORATIVE_BOOK = BLOCKS.register("decorative_book", BlockBookDecoration::new);
}
