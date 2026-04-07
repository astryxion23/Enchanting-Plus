package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

public class ModTileEntities {

    public static final BlockEntityType<TileEntityAdvancedTable> ADVANCED_TABLE = registerAdvancedTable();
    public static final BlockEntityType<TileEntityDecoration> DECORATION = registerDecoration();

    private static BlockEntityType<TileEntityAdvancedTable> registerAdvancedTable() {
        final BlockEntityType<TileEntityAdvancedTable>[] ref = new BlockEntityType[1];
        BlockEntityType<TileEntityAdvancedTable> type = FabricBlockEntityTypeBuilder.create(
                (pos, state) -> new TileEntityAdvancedTable(ref[0], pos, state),
                ModBlocks.ADVANCED_TABLE
        ).build();
        ref[0] = type;
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath("eplus", "advanced_table"), type);
    }

    private static BlockEntityType<TileEntityDecoration> registerDecoration() {
        final BlockEntityType<TileEntityDecoration>[] ref = new BlockEntityType[1];
        BlockEntityType<TileEntityDecoration> type = FabricBlockEntityTypeBuilder.create(
                (pos, state) -> new TileEntityDecoration(ref[0], pos, state),
                ModBlocks.DECORATIVE_BOOK
        ).build();
        ref[0] = type;
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath("eplus", "decorative_book"), type);
    }
}
