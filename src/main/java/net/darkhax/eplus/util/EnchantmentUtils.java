package net.darkhax.eplus.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class EnchantmentUtils {

    public static float getEnchantingPower(Level world, BlockPos pos) {
        float power = 0;
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mut2 = new BlockPos.MutableBlockPos();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    mut.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    mut2.set(pos.getX() + x, pos.getY() + 1, pos.getZ() + z);
                    BlockState state = world.getBlockState(mut2);
                    BlockState stateBelow = world.getBlockState(mut);
                    if (state.isAir() && stateBelow.getEnchantPowerBonus(world, mut) > 0) {
                        power += stateBelow.getEnchantPowerBonus(world, mut);
                    }
                }
            }
        }
        return power;
    }
}
