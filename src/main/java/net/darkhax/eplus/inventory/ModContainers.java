package net.darkhax.eplus.inventory;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    public static ContainerType<ContainerAdvancedTable> ADVANCED_TABLE;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<ContainerType<?>> event) {
        ADVANCED_TABLE = IForgeContainerType.create((windowId, inv, buf) -> {
            BlockPos pos = buf.readBlockPos();
            if (inv.player.level.getBlockEntity(pos) instanceof TileEntityAdvancedTable) {
                TileEntityAdvancedTable te = (TileEntityAdvancedTable) inv.player.level.getBlockEntity(pos);
                EnchantmentLogicController logic = new EnchantmentLogicController(inv.player, inv.player.level, pos, te.getInventory(inv.player));
                return new ContainerAdvancedTable(windowId, inv, logic);
            }
            return null;
        });
        ADVANCED_TABLE.setRegistryName("eplus", "advanced_table");
        event.getRegistry().register(ADVANCED_TABLE);
    }
}
