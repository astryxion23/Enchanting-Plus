package net.darkhax.eplus.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    public static final ResourceKey<net.minecraft.world.level.block.Block> ADVANCED_TABLE_KEY =
            ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("eplus", "advanced_table"));
    public static final ResourceKey<net.minecraft.world.level.block.Block> DECORATIVE_BOOK_KEY =
            ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("eplus", "decorative_book"));

    public static final BlockAdvancedTable ADVANCED_TABLE = registerAdvancedTable();
    public static final BlockBookDecoration DECORATIVE_BOOK = registerDecorativeBook();

    private static BlockAdvancedTable registerAdvancedTable() {
        BlockAdvancedTable block = new BlockAdvancedTable(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 2000.0F)
                .noOcclusion()
                .setId(ADVANCED_TABLE_KEY));
        return Registry.register(BuiltInRegistries.BLOCK, ADVANCED_TABLE_KEY, block);
    }

    private static BlockBookDecoration registerDecorativeBook() {
        BlockBookDecoration block = new BlockBookDecoration(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()
                .strength(1.5F)
                .lightLevel(s -> 15)
                .noOcclusion()
                .setId(DECORATIVE_BOOK_KEY));
        return Registry.register(BuiltInRegistries.BLOCK, DECORATIVE_BOOK_KEY, block);
    }
}
