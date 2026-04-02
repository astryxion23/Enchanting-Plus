package net.darkhax.eplus.creativetab;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CreativeTabEPlus extends ItemGroup {

    public CreativeTabEPlus() {
        super(ItemGroup.getGroupCountSafe(), "eplus");
    }

    @Override
    public ItemStack makeIcon() {
        if (EnchantingPlus.blockAdvancedTable != null) {
            return new ItemStack(EnchantingPlus.blockAdvancedTable);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> items) {
        if (EnchantingPlus.itemAdvancedTable != null) {
            items.add(new ItemStack(EnchantingPlus.itemAdvancedTable));
        }
        if (EnchantingPlus.itemTableUpgrade != null) {
            items.add(new ItemStack(EnchantingPlus.itemTableUpgrade));
        }
        if (EnchantingPlus.itemDecorativeBook != null) {
            items.add(new ItemStack(EnchantingPlus.itemDecorativeBook));
        }
    }
}
