package net.darkhax.eplus.util;

import net.minecraft.inventory.EquipmentSlotType;

public final class EntityUtils {

    public static EquipmentSlotType getEquipmentSlot(int index) {
        switch (index) {
            case 0: return EquipmentSlotType.FEET;
            case 1: return EquipmentSlotType.LEGS;
            case 2: return EquipmentSlotType.CHEST;
            case 3: return EquipmentSlotType.HEAD;
            default: return EquipmentSlotType.HEAD;
        }
    }
}
