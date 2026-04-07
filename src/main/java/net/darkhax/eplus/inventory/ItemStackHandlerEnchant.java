package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ItemStackHandlerEnchant extends SimpleContainer {

    protected BlockEntity tableTile;

    public ItemStackHandlerEnchant(BlockEntity tableTile) {
        super(1);
        this.tableTile = tableTile;
    }

    public ItemStack getEnchantingStack() {
        return this.getItem(0);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot != 0) return false;
        return EnchantingPlus.TEST_ENCHANTABILITY.test(stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (tableTile != null) tableTile.setChanged();
    }

    public void serialize(ValueOutput output) {
        output.store("Stack", ItemStack.CODEC, getItem(0));
    }

    public void deserialize(ValueInput input) {
        setItem(0, input.read("Stack", ItemStack.CODEC).orElse(ItemStack.EMPTY));
    }
}
