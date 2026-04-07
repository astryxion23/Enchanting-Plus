package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;

public class ModContainers {

    public static final ExtendedMenuType<ContainerAdvancedTable, BlockPos> ADVANCED_TABLE = registerAdvancedTable();

    private static ExtendedMenuType<ContainerAdvancedTable, BlockPos> registerAdvancedTable() {
        ExtendedMenuType<ContainerAdvancedTable, BlockPos> type = new ExtendedMenuType<>((windowId, inv, pos) -> {
            BlockEntity be = inv.player.level().getBlockEntity(pos);
            if (be instanceof TileEntityAdvancedTable) {
                TileEntityAdvancedTable te = (TileEntityAdvancedTable) be;
                EnchantmentLogicController logic = new EnchantmentLogicController(inv.player, inv.player.level(), pos, te.getInventory(inv.player));
                return new ContainerAdvancedTable(windowId, inv, logic);
            }
            return null;
        }, BlockPos.STREAM_CODEC.cast());
        return Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath("eplus", "advanced_table"), type);
    }
}
