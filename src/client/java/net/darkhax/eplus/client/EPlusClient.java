package net.darkhax.eplus.client;

import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ModContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import net.fabricmc.api.ClientModInitializer;

public class EPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModContainers.ADVANCED_TABLE, GuiAdvancedTable::new);
        BlockEntityRenderers.register(ModTileEntities.ADVANCED_TABLE, TileEntityAdvancedTableRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.DECORATION, TileEntityDecorationRenderer::new);
    }
}
