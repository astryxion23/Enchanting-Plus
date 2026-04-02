package net.darkhax.eplus.api.event;

import java.util.List;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class AvailableEnchantmentsEvent extends Event {

    private final ItemStack stack;
    private List<Enchantment> enchantments;

    public AvailableEnchantmentsEvent(ItemStack stack, List<Enchantment> enchantments) {
        this.stack = stack;
        this.enchantments = enchantments;
    }

    public List<Enchantment> getEnchantments() { return enchantments; }
    public void setEnchantments(List<Enchantment> enchantments) { this.enchantments = enchantments; }
    public ItemStack getStack() { return stack; }
}
