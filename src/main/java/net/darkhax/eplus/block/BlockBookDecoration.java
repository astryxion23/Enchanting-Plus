package net.darkhax.eplus.block;

import net.darkhax.eplus.ConfigurationHandler;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.darkhax.eplus.block.tileentity.TileEntityWithBook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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

import java.util.Collections;
import java.util.List;

public class BlockBookDecoration extends Block implements EntityBlock {

    private static final VoxelShape BOUNDS = Block.box(4.8, 9.6, 4.8, 9.6, 14.4, 9.6);

    public static final String[] TYPES = new String[] { "eplus", "vanilla", "prismarine", "nether", "tartarite", "white", "metal" };

    public BlockBookDecoration() {
        super(Block.Properties.of().strength(1.5F).lightLevel(s -> 15).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDecoration(ModTileEntities.DECORATION, pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModTileEntities.DECORATION ? (lvl, pos, st, be) -> TileEntityWithBook.tick(lvl, pos, st, (TileEntityWithBook) be) : null;
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityDecoration) {
            double h = ((TileEntityDecoration) te).height;
            return BOUNDS.move(0, h * 16, 0);
        }
        return BOUNDS;
    }

    public float getEnchantPowerBonus(BlockState state, Level world, BlockPos pos) {
        return ConfigurationHandler.floatingBookBonus;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player playerIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && !playerIn.getMainHandItem().isEmpty() && worldIn.getBlockEntity(pos) instanceof TileEntityDecoration) {
            TileEntityDecoration deco = (TileEntityDecoration) worldIn.getBlockEntity(pos);
            if (playerIn.getMainHandItem().getItem() == net.minecraft.world.item.Items.FEATHER)
                deco.increaseHeight();
            else if (playerIn.getMainHandItem().getItem() == net.minecraft.world.item.Items.IRON_INGOT)
                deco.decreaseHeight();
            worldIn.sendBlockUpdated(pos, state, state, 8);
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityDecoration) {
            CustomData data = stack.get(DataComponents.CUSTOM_DATA);
            int variant = (data != null && data.copyTag().contains("Variant")) ? data.copyTag().getInt("Variant") : 0;
            ((TileEntityDecoration) te).variant = Math.min(Math.max(variant, 0), TYPES.length - 1);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder params) {
        BlockEntity te = params.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY);
        if (te instanceof TileEntityDecoration)
            return Collections.singletonList(getData((TileEntityDecoration) te));
        return Collections.singletonList(new ItemStack(this));
    }

    public ItemStack getData(TileEntityDecoration tile) {
        ItemStack stack = new ItemStack(this);
        net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
        tag.putFloat("Height", tile.height);
        tag.putInt("Color", tile.color);
        tag.putInt("Variant", tile.variant);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return te instanceof TileEntityWithBook && ((TileEntityWithBook) te).isOpen() ? 15 : 0;
    }
}
