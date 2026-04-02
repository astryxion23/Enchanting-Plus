package net.darkhax.eplus.client;

import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ModContainers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModContainers.ADVANCED_TABLE, GuiAdvancedTable::new);
        BlockEntityRendererRegistry.register(ModTileEntities.ADVANCED_TABLE, TileEntityAdvancedTableRenderer::new);
        BlockEntityRendererRegistry.register(ModTileEntities.DECORATION, TileEntityDecorationRenderer::new);
    }
}
