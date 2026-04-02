package net.darkhax.eplus.util;

import net.darkhax.eplus.block.BlockBookDecoration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class EnchantmentUtils {

    /** Returns enchant power for a block state (vanilla 1.21.1 has no Block.getEnchantPowerBonus; we emulate it). */
    public static float getEnchantPowerForState(BlockState state, Level world, BlockPos pos) {
        if (state.getBlock() == Blocks.BOOKSHELF) return 1f;
        if (state.getBlock() instanceof BlockBookDecoration) return ((BlockBookDecoration) state.getBlock()).getEnchantPowerBonus(state, world, pos);
        return 0f;
    }

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
                    float bonus = getEnchantPowerForState(stateBelow, world, mut);
                    if (state.isAir() && bonus > 0) {
                        power += bonus;
                    }
                }
            }
        }
        return power;
    }
}
