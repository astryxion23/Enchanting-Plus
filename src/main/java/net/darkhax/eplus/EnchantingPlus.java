package net.darkhax.eplus;

import java.io.File;
import java.util.function.Predicate;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.darkhax.eplus.inventory.ModContainers;
import net.darkhax.eplus.item.ModItems;
import net.darkhax.eplus.network.GuiHandler;
import net.darkhax.eplus.network.NetworkRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod("eplus")
public final class EnchantingPlus {

    public static final Predicate<ItemStack> TEST_ENCHANTABILITY = (stack) ->
            !stack.isEmpty() && !Blacklist.isItemBlacklisted(stack)
                    && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);

    public static final DeferredHolder<Block, Block> blockAdvancedTable = ModBlocks.ADVANCED_TABLE;
    public static final DeferredHolder<Block, Block> blockDecorativeBook = ModBlocks.DECORATIVE_BOOK;
    public static final DeferredHolder<Item, Item> itemAdvancedTable = ModItems.ADVANCED_TABLE;
    public static final DeferredHolder<Item, Item> itemTableUpgrade = ModItems.TABLE_UPGRADE;
    public static final DeferredHolder<Item, Item> itemDecorativeBook = ModItems.DECORATIVE_BOOK;
    public static final DeferredHolder<net.minecraft.world.item.CreativeModeTab, net.minecraft.world.item.CreativeModeTab> creativeTab = CreativeTabEPlus.TAB;

    public EnchantingPlus(IEventBus modBus) {
        modBus.addListener(this::commonSetup);
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTileEntities.BLOCK_ENTITIES.register(modBus);
        ModContainers.MENUS.register(modBus);
        CreativeTabEPlus.CREATIVE_TABS.register(modBus);
        modBus.addListener(NetworkRegistration::register);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            try {
                modBus.register(Class.forName("net.darkhax.eplus.client.ClientSetup"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Client setup class not found", e);
            }
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        File configDir = FMLPaths.CONFIGDIR.get().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        event.enqueueWork(ConfigurationHandler::buildBlacklist);
    }
}
