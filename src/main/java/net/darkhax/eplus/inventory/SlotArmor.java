package net.darkhax.eplus.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class SlotArmor extends Slot {

    private final EquipmentSlotType slotType;

    public SlotArmor(PlayerInventory inv, EquipmentSlotType slotType, int index, int x, int y) {
        super(inv, index, x, y);
        this.slotType = slotType;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getSlot() == slotType;
    }
}
