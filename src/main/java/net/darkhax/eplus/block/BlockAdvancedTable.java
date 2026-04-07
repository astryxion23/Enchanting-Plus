package net.darkhax.eplus.block;

import java.util.Map;
import java.util.UUID;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.util.StackUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

public class BlockAdvancedTable extends Block implements EntityBlock {

    private static final VoxelShape BOUNDS = Block.box(0, 0, 0, 16, 12, 16);

    public BlockAdvancedTable(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityAdvancedTable(ModTileEntities.ADVANCED_TABLE, pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModTileEntities.ADVANCED_TABLE ? (lvl, pos, st, be) -> net.darkhax.eplus.block.tileentity.TileEntityWithBook.tick(lvl, pos, st, (net.darkhax.eplus.block.tileentity.TileEntityWithBook) be) : null;
    }

    private static void spillAdvancedTableContents(Level level, BlockPos pos, TileEntityAdvancedTable table) {
        Map<UUID, ItemStackHandlerEnchant> inventories = table.getInveotries();
        for (ItemStackHandlerEnchant inv : inventories.values()) {
            StackUtils.dropStackInWorld(level, pos, inv.getEnchantingStack());
            inv.setItem(0, ItemStack.EMPTY);
        }
        inventories.clear();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof TileEntityAdvancedTable table)
                spillAdvancedTableContents(level, pos, table);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof TileEntityAdvancedTable table)
            spillAdvancedTableContents(level, pos, table);
        super.wasExploded(level, pos, explosion);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hit) {
        if (!worldIn.isClientSide()) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityAdvancedTable)
                ((ServerPlayer) playerIn).openMenu((TileEntityAdvancedTable) te);
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.CONSUME;
    }
}
