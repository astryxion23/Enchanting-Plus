package net.darkhax.eplus.block.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ValueOutput.ValueOutputList list = output.childrenList("InvList");
        for (Entry<UUID, ItemStackHandlerEnchant> inventory : this.inventories.entrySet()) {
            ValueOutput invOut = list.addChild();
            invOut.store("Owner", UUIDUtil.CODEC, inventory.getKey());
            ValueOutput invData = invOut.child("Inventory");
            inventory.getValue().serialize(invData);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventories.clear();
        for (ValueInput tag : input.childrenListOrEmpty("InvList")) {
            UUID owner = tag.read("Owner", UUIDUtil.CODEC).orElseThrow();
            ItemStackHandlerEnchant inv = new ItemStackHandlerEnchant(this);
            inv.deserialize(tag.childOrEmpty("Inventory"));
            this.inventories.put(owner, inv);
        }
    }
}
