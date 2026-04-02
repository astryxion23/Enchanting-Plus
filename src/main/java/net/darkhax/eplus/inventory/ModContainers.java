package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ModContainers {

    public static final MenuType<ContainerAdvancedTable> ADVANCED_TABLE = Registry.register(BuiltInRegistries.MENU,
            new ResourceLocation("eplus", "advanced_table"),
            new ExtendedScreenHandlerType<>((windowId, inv, buf) -> {
                BlockPos pos = buf.readBlockPos();
                BlockEntity be = inv.player.level().getBlockEntity(pos);
                if (be instanceof TileEntityAdvancedTable) {
                    TileEntityAdvancedTable te = (TileEntityAdvancedTable) be;
                    EnchantmentLogicController logic = new EnchantmentLogicController(inv.player, inv.player.level(), pos, te.getInventory(inv.player));
                    return new ContainerAdvancedTable(windowId, inv, logic);
                }
                return null;
            }));
}
