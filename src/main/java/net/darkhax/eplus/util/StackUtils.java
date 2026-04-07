package net.darkhax.eplus.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

public final class StackUtils {

    public static ItemStack createStackFromString(String str) {
        if (str == null || str.isEmpty()) return ItemStack.EMPTY;
        String[] parts = str.split("#");
        Identifier id = Identifier.parse(parts[0]);
        Optional<Item> itemOpt = BuiltInRegistries.ITEM.getOptional(id);
        if (itemOpt.isEmpty()) return ItemStack.EMPTY;
        Item item = itemOpt.get();
        if (item == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(item, 1);
    }

    public static boolean areStacksSimilarWithPartialNBT(ItemStack a, ItemStack b) {
        if (a.isEmpty() != b.isEmpty()) return false;
        if (a.getItem() != b.getItem()) return false;
        CustomData dataA = a.get(DataComponents.CUSTOM_DATA);
        CustomData dataB = b.get(DataComponents.CUSTOM_DATA);
        if (dataA == null && dataB == null) return true;
        if (dataA == null || dataB == null) return false;
        return dataA.copyTag().equals(dataB.copyTag());
    }

    public static void dropStackInWorld(Level world, BlockPos pos, ItemStack stack) {
        if (world.isClientSide() || stack.isEmpty()) return;
        ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        entity.setDefaultPickUpDelay();
        world.addFreshEntity(entity);
    }
}
