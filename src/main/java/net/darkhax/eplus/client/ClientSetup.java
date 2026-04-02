package net.darkhax.eplus.client;

import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ModContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModContainers.ADVANCED_TABLE.get(), GuiAdvancedTable::new));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntities.ADVANCED_TABLE.get(), TileEntityAdvancedTableRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.DECORATION.get(), TileEntityDecorationRenderer::new);
    }
}
