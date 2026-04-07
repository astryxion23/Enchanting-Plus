package net.darkhax.eplus.block.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.util.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantmentLogicController {

    private final Player player;
    private final Level world;
    private final BlockPos pos;
    private final ItemStackHandlerEnchant inventory;

    private ItemStack inputStack;
    private List<Enchantment> validEnchantments;
    private Map<Enchantment, Integer> initialEnchantments;
    private Map<Enchantment, Integer> itemEnchantments;
    private float enchantmentPower;
    private int cost;

    public EnchantmentLogicController(Player player, Level world, BlockPos pos, ItemStackHandlerEnchant inventory) {
        this.player = player;
        this.world = world;
        this.pos = pos;
        this.inventory = inventory;
        this.validEnchantments = new ArrayList<>();
        this.initialEnchantments = new HashMap<>();
        this.itemEnchantments = new HashMap<>();
        this.onItemUpdated();
    }

    public void onItemUpdated() {
        this.inputStack = this.inventory.getEnchantingStack();
        ItemEnchantments current = EnchantmentHelper.getEnchantmentsForCrafting(this.inputStack);
        this.initialEnchantments = new HashMap<>();
        for (Holder<Enchantment> h : current.keySet())
            this.initialEnchantments.put(h.value(), current.getLevel(h));
        this.itemEnchantments = new HashMap<>(this.initialEnchantments);
        this.validEnchantments = EnchLogic.getValidEnchantments(this.inputStack, this.world, this.pos);
        this.calculateState();
    }

    public void calculateState() {
        this.enchantmentPower = EnchantmentUtils.getEnchantingPower(this.world, this.pos);
        this.cost = 0;
        for (Entry<Enchantment, Integer> newEntry : this.itemEnchantments.entrySet()) {
            int original = this.initialEnchantments.getOrDefault(newEntry.getKey(), 0);
            int newLevels = newEntry.getValue() - original;
            if (newLevels > 0)
                this.cost += EnchLogic.calculateNewEnchCost(this.world.registryAccess(), newEntry.getKey(), newLevels);
        }
        for (Entry<Enchantment, Integer> existingEnch : this.initialEnchantments.entrySet()) {
            if (isCurse(existingEnch.getKey()) && existingEnch.getValue() > 0) {
                int currentCurseLevel = this.itemEnchantments.getOrDefault(existingEnch.getKey(), 0);
                if (currentCurseLevel < existingEnch.getValue())
                    this.cost += EnchLogic.calculateNewEnchCost(this.world.registryAccess(), existingEnch.getKey(), existingEnch.getValue() - currentCurseLevel);
            }
        }
        if (this.enchantmentPower > 0)
            this.cost -= this.getCost() * this.enchantmentPower / 100f;
    }

    public int getCurrentLevel(Enchantment enchant) {
        return this.itemEnchantments.getOrDefault(enchant, 0);
    }

    public void updateEnchantment(Enchantment enchantment, int level) {
        if (level < 1) this.itemEnchantments.remove(enchantment);
        else if (this.validEnchantments.contains(enchantment)) this.itemEnchantments.put(enchantment, level);
        this.calculateState();
    }

    public boolean isValidEnchantment(Enchantment enchantment) { return this.validEnchantments.contains(enchantment); }
    public List<Enchantment> getValidEnchantments() { return this.validEnchantments; }
    public Map<Enchantment, Integer> getInitialEnchantments() { return this.initialEnchantments; }
    public Map<Enchantment, Integer> getCurrentEnchantments() { return this.itemEnchantments; }

    public void enchantItem() {
        if (!this.player.isCreative() && EnchLogic.getExperience(this.player) < this.getCost()) return;
        if (!this.player.isCreative() && this.cost > 0)
            EnchLogic.removeExperience(this.player, this.getCost());
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        for (Entry<Enchantment, Integer> entry : this.itemEnchantments.entrySet()) {
            if (entry.getValue() > 0) {
                Registry<Enchantment> reg = this.world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                Holder<Enchantment> h = reg.wrapAsHolder(entry.getKey());
                mutable.set(h, entry.getValue());
            }
        }
        EnchantmentHelper.setEnchantments(this.inputStack, mutable.toImmutable());
        this.onItemUpdated();
    }

    private boolean isCurse(Enchantment enchantment) {
        Registry<Enchantment> reg = this.world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        return reg.wrapAsHolder(enchantment).is(net.minecraft.tags.EnchantmentTags.CURSE);
    }

    public int getCost() { return this.cost; }
    public float getEnchantmentPower() { return Math.min(this.enchantmentPower, 30f); }
    public ItemStackHandlerEnchant getInventory() { return this.inventory; }
    public BlockPos getPos() { return this.pos; }
    public Level getWorld() { return this.world; }
    public Player getPlayer() { return this.player; }
}
