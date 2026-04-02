package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerEnchant extends ItemStackHandler {

    protected BlockEntity tableTile;

    public ItemStackHandlerEnchant(BlockEntity tableTile) {
        super(1);
        this.tableTile = tableTile;
    }

    public ItemStack getEnchantingStack() {
        return this.getStackInSlot(0);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!EnchantingPlus.TEST_ENCHANTABILITY.test(stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (tableTile != null) tableTile.setChanged();
    }
}
