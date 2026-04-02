package net.darkhax.eplus;

import java.io.File;
import java.util.function.Predicate;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.block.BlockAdvancedTable;
import net.darkhax.eplus.block.BlockBookDecoration;
import net.darkhax.eplus.creativetab.CreativeTabEPlus;
import net.darkhax.eplus.inventory.ModContainers;
import net.darkhax.eplus.item.ItemDecorativeBook;
import net.darkhax.eplus.item.ItemTableUpgrade;
import net.darkhax.eplus.network.GuiHandler;
import net.darkhax.eplus.network.messages.MessageEnchant;
import net.darkhax.eplus.network.messages.MessageSliderUpdate;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

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

    public static Block blockAdvancedTable;
    public static Block blockDecorativeBook;
    public static Item itemAdvancedTable;
    public static Item itemTableUpgrade;
    public static Item itemDecorativeBook;
    public static CreativeTabEPlus creativeTab;

    public EnchantingPlus() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.register(this);  // needed for RegistryEvent.Register (blocks/items)
        MinecraftForge.EVENT_BUS.register(this);
        creativeTab = new CreativeTabEPlus();
        NETWORK.registerMessage(0, MessageEnchant.class, MessageEnchant::encode, MessageEnchant::decode, MessageEnchant::handle);
        NETWORK.registerMessage(1, MessageSliderUpdate.class, MessageSliderUpdate::encode, MessageSliderUpdate::decode, MessageSliderUpdate::handle);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        File configDir = FMLPaths.CONFIGDIR.get().toFile();
        ConfigurationHandler.initConfig(new File(configDir, "eplus.cfg"));
        event.enqueueWork(ConfigurationHandler::buildBlacklist);
    }

    @SubscribeEvent
    public void onBlocksRegister(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        Block advancedTable = new BlockAdvancedTable().setRegistryName("eplus", "advanced_table");
        Block decorativeBook = new BlockBookDecoration().setRegistryName("eplus", "decorative_book");
        r.register(advancedTable);
        r.register(decorativeBook);
        blockAdvancedTable = advancedTable;
        blockDecorativeBook = decorativeBook;
    }

    @SubscribeEvent
    public void onItemsRegister(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        r.register(new BlockItem(blockAdvancedTable, new Item.Properties().tab(creativeTab)).setRegistryName("eplus", "advanced_table"));
        r.register(new ItemTableUpgrade(new Item.Properties().tab(creativeTab).stacksTo(16)).setRegistryName("eplus", "table_upgrade"));
        r.register(new ItemDecorativeBook(blockDecorativeBook, new Item.Properties().tab(creativeTab)).setRegistryName("eplus", "decorative_book"));
        itemAdvancedTable = ForgeRegistries.ITEMS.getValue(new ResourceLocation("eplus", "advanced_table"));
        itemTableUpgrade = ForgeRegistries.ITEMS.getValue(new ResourceLocation("eplus", "table_upgrade"));
        itemDecorativeBook = ForgeRegistries.ITEMS.getValue(new ResourceLocation("eplus", "decorative_book"));
    }
}
