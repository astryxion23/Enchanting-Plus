package net.darkhax.eplus.client;

import net.darkhax.eplus.block.ModTileEntities;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityAdvancedTableRenderer;
import net.darkhax.eplus.block.tileentity.renderer.TileEntityDecorationRenderer;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    // Paths for block atlas: no "textures/" prefix, no ".png" (atlas adds them when loading)
    private static final ResourceLocation[] BOOK_TEXTURES = new ResourceLocation[] {
            new ResourceLocation("eplus", "entity/enchantingplus_book"),
            new ResourceLocation("minecraft", "entity/enchanting_table_book"),
            new ResourceLocation("eplus", "entity/prismarine_book"),
            new ResourceLocation("eplus", "entity/nether_book"),
            new ResourceLocation("eplus", "entity/tartarite_book"),
            new ResourceLocation("eplus", "entity/white_book"),
            new ResourceLocation("eplus", "entity/metal_book")
    };

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.register(ModContainers.ADVANCED_TABLE, GuiAdvancedTable::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.ADVANCED_TABLE, TileEntityAdvancedTableRenderer::new);
            ClientRegistry.bindTileEntityRenderer(ModTileEntities.DECORATION, TileEntityDecorationRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            for (ResourceLocation texture : BOOK_TEXTURES) {
                event.addSprite(texture);
            }
        }
    }
}
