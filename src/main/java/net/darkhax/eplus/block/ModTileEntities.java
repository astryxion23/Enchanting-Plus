package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "eplus");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityAdvancedTable>> ADVANCED_TABLE = BLOCK_ENTITIES.register("advanced_table", () -> {
        final BlockEntityType<TileEntityAdvancedTable>[] ref = new BlockEntityType[1];
        ref[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityAdvancedTable(ref[0], pos, state), ModBlocks.ADVANCED_TABLE.get()).build(null);
        return ref[0];
    });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDecoration>> DECORATION = BLOCK_ENTITIES.register("decorative_book", () -> {
        final BlockEntityType<TileEntityDecoration>[] ref = new BlockEntityType[1];
        ref[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityDecoration(ref[0], pos, state), ModBlocks.DECORATIVE_BOOK.get()).build(null);
        return ref[0];
    });
}
