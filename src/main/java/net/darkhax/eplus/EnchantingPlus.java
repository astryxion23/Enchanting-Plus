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
import net.darkhax.eplus.network.messages.MessageEnchant;
import net.darkhax.eplus.network.messages.MessageSliderUpdate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.RegistryObject;

@Mod("eplus")
public final class EnchantingPlus {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("eplus", "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static final Predicate<ItemStack> TEST_ENCHANTABILITY = (stack) ->
            !stack.isEmpty() && !Blacklist.isItemBlacklisted(stack)
                    && (stack.isEnchantable() || stack.isEnchanted() || stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK);

    public static final RegistryObject<Block> blockAdvancedTable = ModBlocks.ADVANCED_TABLE;
    public static final RegistryObject<Block> blockDecorativeBook = ModBlocks.DECORATIVE_BOOK;
    public static final RegistryObject<Item> itemAdvancedTable = ModItems.ADVANCED_TABLE;
    public static final RegistryObject<Item> itemTableUpgrade = ModItems.TABLE_UPGRADE;
    public static final RegistryObject<Item> itemDecorativeBook = ModItems.DECORATIVE_BOOK;
    public static final RegistryObject<net.minecraft.world.item.CreativeModeTab> creativeTab = CreativeTabEPlus.TAB;

    public EnchantingPlus() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTileEntities.BLOCK_ENTITIES.register(modBus);
        ModContainers.MENUS.register(modBus);
        CreativeTabEPlus.CREATIVE_TABS.register(modBus);
        NETWORK.registerMessage(0, MessageEnchant.class, MessageEnchant::encode, MessageEnchant::decode, MessageEnchant::handle);
        NETWORK.registerMessage(1, MessageSliderUpdate.class, MessageSliderUpdate::encode, MessageSliderUpdate::decode, MessageSliderUpdate::handle);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        File configDir = FMLPaths.CONFIGDIR.get().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        event.enqueueWork(ConfigurationHandler::buildBlacklist);
    }
}
