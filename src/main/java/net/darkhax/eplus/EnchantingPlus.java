package net.darkhax.eplus;

import java.io.File;
import java.util.function.Predicate;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.block.BlockAdvancedTable;
import net.darkhax.eplus.block.BlockBookDecoration;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.darkhax.eplus.inventory.ModContainers;
import net.darkhax.eplus.item.ItemDecorativeBook;
import net.darkhax.eplus.item.ItemTableUpgrade;
import net.darkhax.eplus.item.ModItems;
import net.darkhax.eplus.network.NetworkRegistration;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class EnchantingPlus implements ModInitializer {

    public static final Predicate<ItemStack> TEST_ENCHANTABILITY = (stack) ->
            !stack.isEmpty() && !Blacklist.isItemBlacklisted(stack)
                    && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);

    public static final BlockAdvancedTable blockAdvancedTable = ModBlocks.ADVANCED_TABLE;
    public static final BlockBookDecoration blockDecorativeBook = ModBlocks.DECORATIVE_BOOK;
    public static final BlockItem itemAdvancedTable = ModItems.ADVANCED_TABLE;
    public static final ItemTableUpgrade itemTableUpgrade = ModItems.TABLE_UPGRADE;
    public static final ItemDecorativeBook itemDecorativeBook = ModItems.DECORATIVE_BOOK;
    public static final net.minecraft.world.item.CreativeModeTab creativeTab = CreativeTabEPlus.TAB;

    @Override
    public void onInitialize() {
        ModBlocks.ADVANCED_TABLE.getClass();
        ModItems.ADVANCED_TABLE.getClass();
        ModTileEntities.ADVANCED_TABLE.getClass();
        ModContainers.ADVANCED_TABLE.getClass();
        CreativeTabEPlus.TAB.getClass();
        NetworkRegistration.register();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        ConfigurationHandler.buildBlacklist();
    }
}
