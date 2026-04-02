package net.darkhax.eplus.block;

import java.util.Map;
import java.util.UUID;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.network.GuiHandler;
import net.darkhax.eplus.util.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockAdvancedTable extends Block {

    private static final VoxelShape BOUNDS = Block.box(0, 0, 0, 16, 12, 16);

    public BlockAdvancedTable() {
        super(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).strength(5.0F, 2000.0F).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityAdvancedTable(ModTileEntities.ADVANCED_TABLE);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity te = worldIn.getBlockEntity(pos);
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
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDS;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            TileEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof TileEntityAdvancedTable)
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, new GuiHandler.AdvancedTableContainerProvider((TileEntityAdvancedTable) te, pos), pos);
        }
        return ActionResultType.sidedSuccess(worldIn.isClientSide);
    }
}
