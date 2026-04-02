package net.darkhax.eplus.block.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.inventory.ItemStackHandlerEnchant;
import net.darkhax.eplus.util.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnchantmentLogicController {

    private final PlayerEntity player;
    private final World world;
    private final BlockPos pos;
    private final ItemStackHandlerEnchant inventory;

    private ItemStack inputStack;
    private List<Enchantment> validEnchantments;
    private Map<Enchantment, Integer> initialEnchantments;
    private Map<Enchantment, Integer> itemEnchantments;
    private float enchantmentPower;
    private int cost;

    public EnchantmentLogicController(PlayerEntity player, World world, BlockPos pos, ItemStackHandlerEnchant inventory) {
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
        this.initialEnchantments = new HashMap<>(EnchantmentHelper.getEnchantments(this.inputStack));
        this.itemEnchantments = new HashMap<>(this.initialEnchantments);
        this.validEnchantments = EnchLogic.getValidEnchantments(this.inputStack, this.world, this.pos);
        this.calculateState();
    }

    public void calculateState() {
        this.enchantmentPower = EnchantmentUtils.getEnchantingPower(this.world, this.pos);
        // Use actual slot item for cost calculation (not a preview copy)
        ItemStack stack = this.inventory.getEnchantingStack();
        this.cost = this.calculateTotalCost(stack);
    }

    /**
     * Calculates total XP cost based only on NEW enchantments or level increases.
     * Never charges for enchantments already present at the same level.
     * Uses existing enchantments from the actual item stack.
     */
    public int calculateTotalCost(ItemStack stack) {
        int total = 0;
        if (stack.isEmpty()) {
            return 0;
        }
        Map<Enchantment, Integer> existing = EnchantmentHelper.getEnchantments(stack);
        for (Entry<Enchantment, Integer> e : this.itemEnchantments.entrySet()) {
            Enchantment enchant = e.getKey();
            int selectedLevel = e.getValue();
            int existingLevel = existing.getOrDefault(enchant, 0);
            // Only charge if increasing level
            if (selectedLevel > existingLevel) {
                int levelDifference = selectedLevel - existingLevel;
                total += EnchLogic.calculateNewEnchCost(enchant, levelDifference);
            }
        }
        return Math.max(total, 0);
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
        EnchantmentHelper.setEnchantments(new HashMap<>(), this.inputStack);
        for (Entry<Enchantment, Integer> entry : this.itemEnchantments.entrySet()) {
            if (entry.getValue() > 0)
                this.inputStack.enchant(entry.getKey(), entry.getValue());
        }
        this.onItemUpdated();
    }

    public int getCost() { return this.cost; }
    public float getEnchantmentPower() { return Math.min(this.enchantmentPower, 30f); }
    public ItemStackHandlerEnchant getInventory() { return this.inventory; }
    public BlockPos getPos() { return this.pos; }
    public World getWorld() { return this.world; }
    public PlayerEntity getPlayer() { return this.player; }
}
