package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModTileEntities {

    public static BlockEntityType<TileEntityAdvancedTable> ADVANCED_TABLE;
    public static BlockEntityType<TileEntityDecoration> DECORATION;

    public static void register() {
        final BlockEntityType<TileEntityAdvancedTable>[] refTable = new BlockEntityType[1];
        refTable[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityAdvancedTable(refTable[0], pos, state), ModBlocks.ADVANCED_TABLE).build(null);
        ADVANCED_TABLE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("eplus", "advanced_table"), refTable[0]);

        final BlockEntityType<TileEntityDecoration>[] refDeco = new BlockEntityType[1];
        refDeco[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityDecoration(refDeco[0], pos, state), ModBlocks.DECORATIVE_BOOK).build(null);
        DECORATION = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("eplus", "decorative_book"), refDeco[0]);
    }
}
