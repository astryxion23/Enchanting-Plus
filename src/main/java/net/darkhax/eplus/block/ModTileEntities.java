package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "eplus");

    public static final RegistryObject<BlockEntityType<TileEntityAdvancedTable>> ADVANCED_TABLE = BLOCK_ENTITIES.register("advanced_table", () -> {
        final BlockEntityType<TileEntityAdvancedTable>[] ref = new BlockEntityType[1];
        ref[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityAdvancedTable(ref[0], pos, state), ModBlocks.ADVANCED_TABLE.get()).build(null);
        return ref[0];
    });
    public static final RegistryObject<BlockEntityType<TileEntityDecoration>> DECORATION = BLOCK_ENTITIES.register("decorative_book", () -> {
        final BlockEntityType<TileEntityDecoration>[] ref = new BlockEntityType[1];
        ref[0] = BlockEntityType.Builder.of((pos, state) -> new TileEntityDecoration(ref[0], pos, state), ModBlocks.DECORATIVE_BOOK.get()).build(null);
        return ref[0];
    });
}
