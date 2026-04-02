package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Fabric equivalent of Forge's ItemStackHandler for the enchant table slot.
 * Implements Container for Slot compatibility and keeps NBT serialization format.
 */
public class ItemStackHandlerEnchant implements Container {

    protected final BlockEntity tableTile;
    private final NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public ItemStackHandlerEnchant(BlockEntity tableTile) {
        this.tableTile = tableTile;
    }

    public ItemStack getEnchantingStack() {
        return getItem(0);
    }

    /** Inserts only if stack is enchantable; returns remainder. */
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!EnchantingPlus.TEST_ENCHANTABILITY.test(stack)) return stack;
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = getItem(slot);
        int limit = getMaxStackSize();
        if (!existing.isEmpty() && !ItemStack.isSameItemSameTags(existing, stack)) return stack;
        int toInsert = Math.min(limit - existing.getCount(), stack.getCount());
        if (toInsert <= 0) return stack;
        if (!simulate) {
            if (existing.isEmpty()) setItem(slot, stack.copyWithCount(toInsert));
            else existing.grow(toInsert);
        }
        return stack.getCount() == toInsert ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - toInsert);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        stacks.set(slot, stack == null ? ItemStack.EMPTY : stack);
        if (tableTile != null) tableTile.setChanged();
    }

    public ItemStack getStackInSlot(int slot) {
        return getItem(slot);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < stacks.size() ? stacks.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = getItem(index);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        int toRemove = Math.min(count, stack.getCount());
        ItemStack result = stack.copyWithCount(toRemove);
        stack.shrink(toRemove);
        if (stack.isEmpty()) setItem(index, ItemStack.EMPTY);
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = getItem(index);
        setItem(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < stacks.size()) {
            stacks.set(index, stack == null ? ItemStack.EMPTY : stack);
            if (tableTile != null) tableTile.setChanged();
        }
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
    public void clearContent() {
        setItem(0, ItemStack.EMPTY);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        CompoundTag inv = new CompoundTag();
        inv.put("0", getItem(0).save(new CompoundTag()));
        nbt.put("Items", inv);
        nbt.putInt("Size", 1);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("Items", 10)) {
            CompoundTag items = nbt.getCompound("Items");
            for (String key : items.getAllKeys()) {
                int i = Integer.parseInt(key);
                if (i >= 0 && i < stacks.size())
                    stacks.set(i, ItemStack.of(items.getCompound(key)));
            }
        }
        if (tableTile != null) tableTile.setChanged();
    }
}
