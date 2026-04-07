package net.darkhax.eplus.util;

import net.darkhax.eplus.block.BlockBookDecoration;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class EnchantmentUtils {

    /**
     * Enchanting power contributed by a block below an air column next to the table (vanilla bookshelf = 1).
     */
    public static float getEnchantPowerBonus(BlockState stateBelow, Level world, BlockPos shelfPos) {
        if (stateBelow.getBlock() instanceof BlockBookDecoration book) {
            return book.getEnchantPowerBonus(stateBelow, world, shelfPos);
        }
        if (stateBelow.is(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
            return 1.0f;
        }
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
                    if (state.isAir() && EnchantingTableBlock.isValidBookShelf(world, pos, mut)) {
                        float bonus = getEnchantPowerBonus(stateBelow, world, mut);
                        if (bonus > 0) power += bonus;
                    }
                }
            }
        }
        return power;
    }
}
