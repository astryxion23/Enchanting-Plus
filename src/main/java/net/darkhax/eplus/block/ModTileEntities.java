package net.darkhax.eplus.block;

import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.block.tileentity.TileEntityDecoration;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities {

    public static TileEntityType<TileEntityAdvancedTable> ADVANCED_TABLE;
    public static TileEntityType<TileEntityDecoration> DECORATION;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<TileEntityType<?>> event) {
        Block advBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("eplus", "advanced_table"));
        TileEntityType<TileEntityAdvancedTable> advType = TileEntityType.Builder.of(() -> new TileEntityAdvancedTable(ADVANCED_TABLE), advBlock).build(null);
        advType.setRegistryName("eplus", "advanced_table");
        ADVANCED_TABLE = advType;
        event.getRegistry().register(advType);

        Block decBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("eplus", "decorative_book"));
        TileEntityType<TileEntityDecoration> decType = TileEntityType.Builder.of(() -> new TileEntityDecoration(DECORATION), decBlock).build(null);
        decType.setRegistryName("eplus", "decorative_book");
        DECORATION = decType;
        event.getRegistry().register(decType);
    }
}
