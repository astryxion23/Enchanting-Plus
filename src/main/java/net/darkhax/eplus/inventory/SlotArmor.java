package net.darkhax.eplus.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

public class SlotArmor extends Slot {

    private final EquipmentSlot slotType;

    public SlotArmor(Inventory inv, EquipmentSlot slotType, int index, int x, int y) {
        super(inv, index, x, y);
        this.slotType = slotType;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot() == this.slotType;
    }
}
