package net.darkhax.eplus.util;

import net.minecraft.enchantment.Enchantment;

public final class EnchantData {

    public final Enchantment enchantment;
    public final int enchantmentLevel;

    public EnchantData(Enchantment enchantment, int enchantmentLevel) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
    }
}
