package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModContainers {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, "eplus");

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerAdvancedTable>> ADVANCED_TABLE = MENUS.register("advanced_table",
            () -> IMenuTypeExtension.create((windowId, inv, buf) -> {
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
