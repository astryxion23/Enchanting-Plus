package net.darkhax.eplus.client;

import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ModContainers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {});
    }

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModContainers.ADVANCED_TABLE.get(), GuiAdvancedTable::new);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntities.ADVANCED_TABLE.get(), TileEntityAdvancedTableRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.DECORATION.get(), TileEntityDecorationRenderer::new);
    }
}
