package net.darkhax.eplus.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public final class RegistryUtils {

    public static Enchantment getEnchantment(String id) {
        if (id == null || id.isEmpty()) return null;
        ResourceLocation loc = id.contains(":") ? new ResourceLocation(id) : new ResourceLocation("minecraft", id);
        return ForgeRegistries.ENCHANTMENTS.getValue(loc);
    }
}
