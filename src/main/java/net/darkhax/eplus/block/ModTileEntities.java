package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("unchecked")
public class ModTileEntities {

    public static final BlockEntityType<TileEntityAdvancedTable> ADVANCED_TABLE;
    public static final BlockEntityType<TileEntityDecoration> DECORATION;

    static {
        BlockEntityType<TileEntityAdvancedTable>[] aRef = (BlockEntityType<TileEntityAdvancedTable>[]) new BlockEntityType<?>[1];
        aRef[0] = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation("eplus", "advanced_table"),
                BlockEntityType.Builder.of((pos, state) -> new TileEntityAdvancedTable(aRef[0], pos, state), ModBlocks.ADVANCED_TABLE).build(null));
        ADVANCED_TABLE = aRef[0];

        BlockEntityType<TileEntityDecoration>[] dRef = (BlockEntityType<TileEntityDecoration>[]) new BlockEntityType<?>[1];
        dRef[0] = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation("eplus", "decorative_book"),
                BlockEntityType.Builder.of((pos, state) -> new TileEntityDecoration(dRef[0], pos, state), ModBlocks.DECORATIVE_BOOK).build(null));
        DECORATION = dRef[0];
    }
}
