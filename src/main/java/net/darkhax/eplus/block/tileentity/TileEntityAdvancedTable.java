package net.darkhax.eplus.block.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

public class TileEntityAdvancedTable extends TileEntityWithBook {

    private final Map<UUID, ItemStackHandlerEnchant> inventories = new HashMap<>();

    public TileEntityAdvancedTable(TileEntityType<?> type) {
        super(type);
    }

    public ItemStackHandlerEnchant getInventory(PlayerEntity player) {
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
    public CompoundNBT save(CompoundNBT dataTag) {
        super.save(dataTag);
        ListNBT list = new ListNBT();
        for (Entry<UUID, ItemStackHandlerEnchant> inventory : this.inventories.entrySet()) {
            CompoundNBT invTag = new CompoundNBT();
            invTag.putUUID("Owner", inventory.getKey());
            invTag.put("Inventory", inventory.getValue().serializeNBT());
            list.add(invTag);
        }
        dataTag.put("InvList", list);
        return dataTag;
    }

    @Override
    public void load(net.minecraft.block.BlockState state, CompoundNBT dataTag) {
        super.load(state, dataTag);
        this.inventories.clear();
        ListNBT list = dataTag.getList("InvList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT tag = list.getCompound(i);
            UUID owner = tag.getUUID("Owner");
            ItemStackHandlerEnchant inv = new ItemStackHandlerEnchant(this);
            inv.deserializeNBT(tag.getCompound("Inventory"));
            this.inventories.put(owner, inv);
        }
    }
}
