package net.darkhax.eplus.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class EnchantmentUtils {

    public static float getEnchantingPower(World world, BlockPos pos) {
        float power = 0;
        BlockPos.Mutable mut = new BlockPos.Mutable();
        BlockPos.Mutable mut2 = new BlockPos.Mutable();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    mut.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    mut2.set(pos.getX() + x, pos.getY() + 1, pos.getZ() + z);
                    BlockState state = world.getBlockState(mut2);
                    BlockState stateBelow = world.getBlockState(mut);
                    if (state.isAir(world, mut2) && stateBelow.getEnchantPowerBonus(world, mut) > 0) {
                        power += stateBelow.getEnchantPowerBonus(world, mut);
                    }
                }
            }
        }
        return power;
    }
}
