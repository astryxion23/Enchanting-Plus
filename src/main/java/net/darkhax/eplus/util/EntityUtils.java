package net.darkhax.eplus.util;

import net.minecraft.world.entity.EquipmentSlot;

public final class EntityUtils {

    public static EquipmentSlot getEquipmentSlot(int index) {
        switch (index) {
            case 0: return EquipmentSlot.FEET;
            case 1: return EquipmentSlot.LEGS;
            case 2: return EquipmentSlot.CHEST;
            case 3: return EquipmentSlot.HEAD;
            default: return EquipmentSlot.HEAD;
        }
    }
}
