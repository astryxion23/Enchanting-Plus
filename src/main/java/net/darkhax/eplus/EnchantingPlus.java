package net.darkhax.eplus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.api.event.EnchantmentCostEvent;
import net.darkhax.eplus.api.event.InfoBoxEvent;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.darkhax.eplus.inventory.ModContainers;
import net.darkhax.eplus.item.ModItems;
import net.darkhax.eplus.network.messages.MessageEnchant;
import net.darkhax.eplus.network.messages.MessageSliderUpdate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public final class EnchantingPlus implements ModInitializer {

    public static final String MOD_ID = "eplus";

    /** Fabric networking channel; payloads registered in client/server. */
    public static final ResourceLocation CHANNEL_ENCHANT = new ResourceLocation(MOD_ID, "enchant");
    public static final ResourceLocation CHANNEL_SLIDER = new ResourceLocation(MOD_ID, "slider");

    public static final Predicate<ItemStack> TEST_ENCHANTABILITY = (stack) ->
            !stack.isEmpty() && !Blacklist.isItemBlacklisted(stack)
                    && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);

    public static final Block blockAdvancedTable = ModBlocks.ADVANCED_TABLE;
    public static final Block blockDecorativeBook = ModBlocks.DECORATIVE_BOOK;
    public static final Item itemAdvancedTable = ModItems.ADVANCED_TABLE;
    public static final Item itemTableUpgrade = ModItems.TABLE_UPGRADE;
    public static final Item itemDecorativeBook = ModItems.DECORATIVE_BOOK;
    public static final net.minecraft.world.item.CreativeModeTab creativeTab = CreativeTabEPlus.TAB;

    /** Listeners for EnchantmentCostEvent (Fabric replacement for Forge event bus). */
    public static final List<Consumer<EnchantmentCostEvent>> ENCHANTMENT_COST_LISTENERS = new ArrayList<>();
    /** Listeners for InfoBoxEvent (client-only). */
    public static final List<Consumer<InfoBoxEvent>> INFO_BOX_LISTENERS = new ArrayList<>();

    @Override
    public void onInitialize() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ConfigurationHandler.buildBlacklist());
        MessageEnchant.register();
        MessageSliderUpdate.register();
    }
}
