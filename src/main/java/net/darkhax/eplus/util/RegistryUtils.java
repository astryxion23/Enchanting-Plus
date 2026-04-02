package net.darkhax.eplus.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public final class RegistryUtils {

    public static Enchantment getEnchantment(RegistryAccess registryAccess, String id) {
        if (id == null || id.isEmpty() || registryAccess == null) return null;
        ResourceLocation loc = id.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath("minecraft", id);
        return registryAccess.registryOrThrow(Registries.ENCHANTMENT).get(loc);
    }
}
