package net.darkhax.eplus.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.Enchantment;

public final class RegistryUtils {

    public static Enchantment getEnchantment(RegistryAccess registryAccess, String id) {
        if (id == null || id.isEmpty() || registryAccess == null) return null;
        Identifier loc = id.contains(":") ? Identifier.parse(id) : Identifier.fromNamespaceAndPath("minecraft", id);
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOptional(loc).orElse(null);
    }
}
