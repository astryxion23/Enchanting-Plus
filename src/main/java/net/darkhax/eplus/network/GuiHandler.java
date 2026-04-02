package net.darkhax.eplus.network;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public final class GuiHandler {

    public static final int ADVANCED_TABLE = 0;

    public static class AdvancedTableContainerProvider implements ExtendedScreenHandlerFactory {
        private final TileEntityAdvancedTable te;
        private final BlockPos pos;

        public AdvancedTableContainerProvider(TileEntityAdvancedTable te, BlockPos pos) {
            this.te = te;
            this.pos = pos;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("tile.eplus.advanced.table.name");
        }

        @Override
        public net.darkhax.eplus.inventory.ContainerAdvancedTable createMenu(int id, Inventory inv, Player player) {
            EnchantmentLogicController logic = new EnchantmentLogicController(player, te.getLevel(), pos, te.getInventory(player));
            return new net.darkhax.eplus.inventory.ContainerAdvancedTable(id, inv, logic);
        }

        @Override
        public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }
}
