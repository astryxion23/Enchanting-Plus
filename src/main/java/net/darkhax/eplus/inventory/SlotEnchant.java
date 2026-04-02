package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SlotEnchant extends SlotItemHandler {

    private final EnchantmentLogicController logic;

    public SlotEnchant(EnchantmentLogicController logic, int index, int xPosition, int yPosition) {
        super(logic.getInventory(), index, xPosition, yPosition);
        this.logic = logic;
    }

    @Override
    public void setChanged() {
        this.logic.onItemUpdated();
        super.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return EnchantingPlus.TEST_ENCHANTABILITY.test(stack);
    }
}
