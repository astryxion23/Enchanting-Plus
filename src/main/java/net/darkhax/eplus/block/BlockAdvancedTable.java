package net.darkhax.eplus.block;

import java.util.Map;
import java.util.UUID;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.network.GuiHandler;
import net.darkhax.eplus.util.StackUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockAdvancedTable extends Block implements EntityBlock {

    private static final VoxelShape BOUNDS = Block.box(0, 0, 0, 16, 12, 16);

    public BlockAdvancedTable() {
        super(Block.Properties.of().mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).strength(5.0F, 2000.0F).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityAdvancedTable(ModTileEntities.ADVANCED_TABLE, pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModTileEntities.ADVANCED_TABLE ? (lvl, pos, st, be) -> net.darkhax.eplus.block.tileentity.TileEntityWithBook.tick(lvl, pos, st, (net.darkhax.eplus.block.tileentity.TileEntityWithBook) be) : null;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityAdvancedTable) {
                Map<UUID, ItemStackHandlerEnchant> inventories = ((TileEntityAdvancedTable) te).getInveotries();
                for (ItemStackHandlerEnchant inv : inventories.values()) {
                    StackUtils.dropStackInWorld(worldIn, pos, inv.getEnchantingStack());
                    inv.setStackInSlot(0, ItemStack.EMPTY);
                }
                inventories.clear();
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityAdvancedTable)
                ((ServerPlayer) playerIn).openMenu(new GuiHandler.AdvancedTableContainerProvider((TileEntityAdvancedTable) te, pos));
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }
}
