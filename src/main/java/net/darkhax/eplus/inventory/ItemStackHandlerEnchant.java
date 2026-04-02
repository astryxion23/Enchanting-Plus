package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemStackHandlerEnchant implements Container {

    protected BlockEntity tableTile;
    private ItemStack stack = ItemStack.EMPTY;

    public ItemStackHandlerEnchant(BlockEntity tableTile) {
        this.tableTile = tableTile;
    }

    public ItemStack getEnchantingStack() {
        return getItem(0);
    }

    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? stack : ItemStack.EMPTY;
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot == 0) {
            this.stack = stack;
            setChanged();
        }
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot != 0 || !EnchantingPlus.TEST_ENCHANTABILITY.test(stack)) return stack;
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = this.stack;
        if (!existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, stack)) return stack;
        int limit = getSlotLimit(slot);
        int toAdd = Math.min(stack.getCount(), limit - existing.getCount());
        if (toAdd <= 0) return stack;
        if (!simulate) {
            if (existing.isEmpty()) setStackInSlot(0, stack.copyWithCount(toAdd));
            else existing.grow(toAdd);
            if (tableTile != null) tableTile.setChanged();
        }
        return toAdd >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - toAdd);
    }

    public int getSlotLimit(int slot) {
        return 1;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (!stack.isEmpty()) stack.save(registries, tag);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
        stack = tag.isEmpty() ? ItemStack.EMPTY : ItemStack.parse(registries, tag).orElse(ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public ItemStack getItem(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != 0 || stack.isEmpty()) return ItemStack.EMPTY;
        int toRemove = Math.min(amount, stack.getCount());
        ItemStack result = stack.copyWithCount(toRemove);
        stack = toRemove >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - toRemove);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) return ItemStack.EMPTY;
        ItemStack old = stack;
        stack = ItemStack.EMPTY;
        return old;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        if (!EnchantingPlus.TEST_ENCHANTABILITY.test(stack) && !stack.isEmpty()) return;
        if (!stack.isEmpty() && stack.getCount() > 1) stack = stack.copyWithCount(1);
        this.stack = stack;
        setChanged();
    }

    @Override
    public void setChanged() {
        if (tableTile != null) tableTile.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void clearContent() {
        stack = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
