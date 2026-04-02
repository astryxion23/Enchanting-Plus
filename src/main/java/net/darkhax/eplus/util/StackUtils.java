package net.darkhax.eplus.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public final class StackUtils {

    public static ItemStack createStackFromString(String str) {
        if (str == null || str.isEmpty()) return ItemStack.EMPTY;
        String[] parts = str.split("#");
        ResourceLocation id = new ResourceLocation(parts[0]);
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == Items.AIR) return ItemStack.EMPTY;
        int meta = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        return new ItemStack(item, 1);
    }

    public static boolean areStacksSimilarWithPartialNBT(ItemStack a, ItemStack b) {
        if (a.isEmpty() != b.isEmpty()) return false;
        if (a.getItem() != b.getItem()) return false;
        if (a.getTag() == null && b.getTag() == null) return true;
        if (a.getTag() == null || b.getTag() == null) return false;
        return a.getTag().equals(b.getTag());
    }

    public static void dropStackInWorld(World world, BlockPos pos, ItemStack stack) {
        if (world.isClientSide || stack.isEmpty()) return;
        net.minecraft.entity.item.ItemEntity entity = new net.minecraft.entity.item.ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        entity.setDefaultPickUpDelay();
        world.addFreshEntity(entity);
    }
}
