package net.darkhax.eplus.inventory;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "eplus");

    public static final RegistryObject<MenuType<ContainerAdvancedTable>> ADVANCED_TABLE = MENUS.register("advanced_table",
            () -> IForgeMenuType.create((windowId, inv, buf) -> {
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
