package net.darkhax.eplus.block;

import net.darkhax.eplus.ConfigurationHandler;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BlockBookDecoration extends Block {

    private static final VoxelShape BOUNDS = Block.box(4.8, 9.6, 4.8, 9.6, 14.4, 9.6);

    public static final String[] TYPES = new String[] { "eplus", "vanilla", "prismarine", "nether", "tartarite", "white", "metal" };

    public BlockBookDecoration() {
        super(Block.Properties.of(Material.WOOD).strength(1.5F).lightLevel(s -> 15).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityDecoration(ModTileEntities.DECORATION);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityDecoration) {
            double h = ((TileEntityDecoration) te).height;
            return BOUNDS.move(0, h * 16, 0);
        }
        return BOUNDS;
    }

    public float getEnchantPowerBonus(BlockState state, World world, BlockPos pos) {
        return ConfigurationHandler.floatingBookBonus;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && !playerIn.getItemInHand(hand).isEmpty() && worldIn.getBlockEntity(pos) instanceof TileEntityDecoration) {
            TileEntityDecoration deco = (TileEntityDecoration) worldIn.getBlockEntity(pos);
            if (playerIn.getItemInHand(hand).getItem() == net.minecraft.item.Items.FEATHER)
                deco.increaseHeight();
            else if (playerIn.getItemInHand(hand).getItem() == net.minecraft.item.Items.IRON_INGOT)
                deco.decreaseHeight();
            worldIn.sendBlockUpdated(pos, state, state, 8);
        }
        return ActionResultType.sidedSuccess(worldIn.isClientSide);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityDecoration) {
            int variant = stack.hasTag() && stack.getTag().contains("Variant") ? stack.getTag().getInt("Variant") : stack.getDamageValue();
            ((TileEntityDecoration) te).variant = Math.min(Math.max(variant, 0), TYPES.length - 1);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity te = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
        if (te instanceof TileEntityDecoration)
            return Collections.singletonList(getData((TileEntityDecoration) te));
        return Collections.singletonList(new ItemStack(this));
    }

    public ItemStack getData(TileEntityDecoration tile) {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putFloat("Height", tile.height);
        stack.getOrCreateTag().putInt("Color", tile.color);
        stack.getOrCreateTag().putInt("Variant", tile.variant);
        return stack;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        return te instanceof TileEntityWithBook && ((TileEntityWithBook) te).isOpen() ? 15 : 0;
    }
}
