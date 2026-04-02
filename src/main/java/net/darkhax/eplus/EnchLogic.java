package net.darkhax.eplus;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.api.event.EnchantmentCostEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public final class EnchLogic {

    public static int calculateNewEnchCost(Enchantment enchantment, int level) {
        int cost = ConfigurationHandler.baseCost;
        Enchantment.Rarity rarity = enchantment.getRarity();
        int weight = rarity != null ? rarity.getWeight() : 10;
        cost *= Math.max(11 - weight, 1);
        cost *= level;
        cost *= ConfigurationHandler.costFactor;
        if (enchantment.isCurse()) {
            cost *= ConfigurationHandler.curseFactor;
        } else if (enchantment.isTreasureOnly()) {
            cost *= ConfigurationHandler.treasureFactor;
        }
        EnchantmentCostEvent event = new EnchantmentCostEvent(cost, enchantment, level);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCost();
    }

    public static List<Enchantment> getValidEnchantments(ItemStack stack, World world, BlockPos pos) {
        List<Enchantment> enchList = new ArrayList<>();
        if (!stack.isEmpty() && (stack.isEnchantable() || stack.isEnchanted())) {
            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                if (Blacklist.isEnchantmentBlacklisted(enchantment) || !enchantment.canEnchant(stack))
                    continue;
                // Treasure-only (e.g. Mending): always allow on advanced table; curses need wicked night
                boolean allowTreasure = !enchantment.isTreasureOnly()
                    || isCurse(world, enchantment)
                    || (enchantment.isTreasureOnly() && !enchantment.isCurse());
                if (allowTreasure) {
                    enchList.add(enchantment);
                }
            }
        }
        return enchList;
    }

    public static boolean isCurse(World world, Enchantment enchantment) {
        return enchantment.isCurse() && isWikedNight(world);
    }

    public static boolean isTreasuresAvailable(Enchantment enchantment, World world, BlockPos pos, BlockPos down) {
        if (enchantment.isCurse() || !enchantment.isTreasureOnly() || world.isDay()) return false;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos currentPos = down.offset(x, 0, z);
                Block block = world.getBlockState(currentPos).getBlock();
                BlockState stateAt = world.getBlockState(currentPos);
                if (stateAt.getEnchantPowerBonus(world, currentPos) <= 0)
                    return false;
            }
        }
        return true;
    }

    public static boolean isWikedNight(World world) {
        long time = world.getDayTime() % 24000L;
        boolean isNightRange = time >= 12000 && time <= 18000;
        long dayIndex = world.getDayTime() / 24000L;
        int moonPhase = (int) (dayIndex % 8L);
        return moonPhase == 0 && isNightRange;
    }

    public static int getExperience(PlayerEntity player) {
        int level = player.experienceLevel;
        int xpForNext = getExperienceForLevels(level + 1) - getExperienceForLevels(level);
        return (int) (getExperienceForLevels(level) + player.experienceProgress * xpForNext);
    }

    public static void removeExperience(PlayerEntity player, int amount) {
        addExperience(player, -amount);
    }

    public static void addExperience(PlayerEntity player, int amount) {
        int experience = getExperience(player) + amount;
        int newLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevels(newLevel);
        int xpForNext = getExperienceForLevels(newLevel + 1) - expForLevel;
        player.experienceLevel = newLevel;
        player.experienceProgress = xpForNext > 0 ? (float) (experience - expForLevel) / (float) xpForNext : 0f;
    }

    public static int getExperienceForLevels(int level) {
        if (level == 0) return 0;
        if (level > 0 && level < 17) return level * level + 6 * level;
        if (level > 16 && level < 32) return (int) (2.5 * level * level - 40.5 * level + 360);
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }

    public static int getLevelForExperience(int experience) {
        int level = 0;
        while (getExperienceForLevels(level) <= experience) level++;
        return level - 1;
    }
}
