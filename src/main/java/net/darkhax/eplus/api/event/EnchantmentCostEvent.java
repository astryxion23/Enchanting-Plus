package net.darkhax.eplus.api.event;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.Event;

public class EnchantmentCostEvent extends Event {

    private final Enchantment enchantment;
    private final int level;
    private final int originalCost;
    private int cost;

    public EnchantmentCostEvent(int cost, Enchantment enchantment, int level) {
        this.cost = cost;
        this.originalCost = cost;
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() { return enchantment; }
    public int getLevel() { return level; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getOriginalCost() { return originalCost; }
}
