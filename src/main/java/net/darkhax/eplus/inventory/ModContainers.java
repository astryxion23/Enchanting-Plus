package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;

public class ModContainers {

    public static MenuType<ContainerAdvancedTable> ADVANCED_TABLE;

    public static void register() {
        ADVANCED_TABLE = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath("eplus", "advanced_table"),
                new ExtendedScreenHandlerType<>((windowId, inv, pos) -> {
                    BlockEntity be = inv.player.level().getBlockEntity(pos);
                    if (be instanceof TileEntityAdvancedTable) {
                        TileEntityAdvancedTable te = (TileEntityAdvancedTable) be;
                        EnchantmentLogicController logic = new EnchantmentLogicController(inv.player, inv.player.level(), pos, te.getInventory(inv.player));
                        return new ContainerAdvancedTable(windowId, inv, logic);
                    }
                    return null;
                }, BlockPos.STREAM_CODEC));
    }
}
