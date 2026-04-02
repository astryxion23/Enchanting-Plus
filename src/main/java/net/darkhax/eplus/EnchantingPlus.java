package net.darkhax.eplus;

import java.io.File;
import java.util.function.Predicate;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.darkhax.eplus.inventory.ModContainers;
import net.darkhax.eplus.item.ModItems;
import net.darkhax.eplus.network.NetworkRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.fabricmc.api.ModInitializer;

public final class EnchantingPlus implements ModInitializer {

    public static final Predicate<ItemStack> TEST_ENCHANTABILITY = (stack) ->
            !stack.isEmpty() && !Blacklist.isItemBlacklisted(stack)
                    && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);

    public static Block blockAdvancedTable;
    public static Block blockDecorativeBook;
    public static Item itemAdvancedTable;
    public static Item itemTableUpgrade;
    public static Item itemDecorativeBook;
    public static net.minecraft.world.item.CreativeModeTab creativeTab;

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModTileEntities.register();
        ModItems.register();
        ModContainers.register();
        CreativeTabEPlus.register();

        blockAdvancedTable = ModBlocks.ADVANCED_TABLE;
        blockDecorativeBook = ModBlocks.DECORATIVE_BOOK;
        itemAdvancedTable = ModItems.ADVANCED_TABLE;
        itemTableUpgrade = ModItems.TABLE_UPGRADE;
        itemDecorativeBook = ModItems.DECORATIVE_BOOK;
        creativeTab = CreativeTabEPlus.TAB;

        File configDir = net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        ConfigurationHandler.buildBlacklist();

        NetworkRegistration.register();
    }
}
