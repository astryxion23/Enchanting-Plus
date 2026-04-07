package net.darkhax.eplus;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.api.event.EnchantmentCostEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.NeoForge;

public final class EnchLogic {

    public static int calculateNewEnchCost(Enchantment enchantment, int level) {
        return calculateNewEnchCost(null, enchantment, level);
    }

    public static int calculateNewEnchCost(RegistryAccess registryAccess, Enchantment enchantment, int level) {
        int cost = ConfigurationHandler.baseCost;
        int weight = 10; // default; 1.21 uses data-driven weights
        cost *= Math.max(11 - weight, 1);
        cost *= level;
        cost *= ConfigurationHandler.costFactor;
        if (registryAccess != null) {
            if (isCurseEnchantment(registryAccess, enchantment)) cost *= ConfigurationHandler.curseFactor;
            else if (isTreasureOnlyEnchantment(registryAccess, enchantment)) cost *= ConfigurationHandler.treasureFactor;
        }
        EnchantmentCostEvent event = new EnchantmentCostEvent(cost, enchantment, level);
        NeoForge.EVENT_BUS.post(event);
        return event.getCost();
    }

    public static boolean isCurseEnchantment(RegistryAccess registryAccess, Enchantment enchantment) {
        Registry<Enchantment> reg = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = reg.wrapAsHolder(enchantment);
        return holder.is(EnchantmentTags.CURSE);
    }

    public static boolean isTreasureOnlyEnchantment(RegistryAccess registryAccess, Enchantment enchantment) {
        Registry<Enchantment> reg = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = reg.wrapAsHolder(enchantment);
        return holder.is(EnchantmentTags.TREASURE);
    }


    public static List<Enchantment> getValidEnchantments(ItemStack stack, Level world, BlockPos pos) {
        List<Enchantment> enchList = new ArrayList<>();
        if (!stack.isEmpty() && (stack.isEnchantable() || stack.isEnchanted())) {
            Registry<Enchantment> reg = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            ConfigurationHandler.buildEnchantmentBlacklist(world.registryAccess());
            for (Enchantment enchantment : reg) {
                if (Blacklist.isEnchantmentBlacklisted(enchantment) || !enchantment.canEnchant(stack))
                    continue;
                // Treasure-only (e.g. Mending): always allow on advanced table; curses need wicked night
                boolean isTreasure = isTreasureOnlyEnchantment(world.registryAccess(), enchantment);
                boolean isCurseEnch = isCurseEnchantment(world.registryAccess(), enchantment);
                boolean allowTreasure = !isTreasure || isCurse(world, enchantment) || (isTreasure && !isCurseEnch);
                if (allowTreasure) {
                    enchList.add(enchantment);
                }
            }
        }
        return enchList;
    }

    public static boolean isCurse(Level world, Enchantment enchantment) {
        return isCurseEnchantment(world.registryAccess(), enchantment) && isWikedNight(world);
    }

    public static boolean isTreasuresAvailable(Enchantment enchantment, Level world, BlockPos pos, BlockPos down) {
        if (isCurseEnchantment(world.registryAccess(), enchantment) || !isTreasureOnlyEnchantment(world.registryAccess(), enchantment) || world.isBrightOutside()) return false;
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

    public static boolean isWikedNight(Level world) {
        long dayTime = world.getDefaultClockTime();
        long time = dayTime % 24000L;
        boolean isNightRange = time >= 12000 && time <= 18000;
        long dayIndex = dayTime / 24000L;
        int moonPhase = (int) (dayIndex % 8L);
        return moonPhase == 0 && isNightRange;
    }

    public static int getExperience(Player player) {
        int level = player.experienceLevel;
        int xpForNext = getExperienceForLevels(level + 1) - getExperienceForLevels(level);
        return (int) (getExperienceForLevels(level) + player.experienceProgress * xpForNext);
    }

    public static void removeExperience(Player player, int amount) {
        addExperience(player, -amount);
    }

    public static void addExperience(Player player, int amount) {
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
