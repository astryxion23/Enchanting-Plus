package net.darkhax.eplus.block.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvancedTable extends TileEntityWithBook {

    private final Map<UUID, ItemStackHandlerEnchant> inventories = new HashMap<>();

    public TileEntityAdvancedTable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemStackHandlerEnchant getInventory(Player player) {
        ItemStackHandlerEnchant inv = this.inventories.getOrDefault(player.getUUID(), new ItemStackHandlerEnchant(this));
        this.inventories.put(player.getUUID(), inv);
        return inv;
    }

    public Map<UUID, ItemStackHandlerEnchant> getInveotries() {
        return this.inventories;
    }

    /**
     * First non-empty stack in slot 0 from any player's inventory, for floating item display.
     */
    public ItemStack getDisplayStack() {
        for (ItemStackHandlerEnchant inv : this.inventories.values()) {
            ItemStack stack = inv.getStackInSlot(0);
            if (!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag dataTag, HolderLookup.Provider registries) {
        super.saveAdditional(dataTag, registries);
        ListTag list = new ListTag();
        for (Entry<UUID, ItemStackHandlerEnchant> inventory : this.inventories.entrySet()) {
            CompoundTag invTag = new CompoundTag();
            invTag.putUUID("Owner", inventory.getKey());
            invTag.put("Inventory", inventory.getValue().serializeNBT(registries));
            list.add(invTag);
        }
        dataTag.put("InvList", list);
    }

    @Override
    protected void loadAdditional(CompoundTag dataTag, HolderLookup.Provider registries) {
        super.loadAdditional(dataTag, registries);
        this.inventories.clear();
        ListTag list = dataTag.getList("InvList", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            UUID owner = tag.getUUID("Owner");
            ItemStackHandlerEnchant inv = new ItemStackHandlerEnchant(this);
            inv.deserializeNBT(registries, tag.getCompound("Inventory"));
            this.inventories.put(owner, inv);
        }
    }
}
