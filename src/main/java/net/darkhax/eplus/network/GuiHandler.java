package net.darkhax.eplus.network;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.block.tileentity.TileEntityAdvancedTable;
import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public final class GuiHandler {

    public static final int ADVANCED_TABLE = 0;

    public static class AdvancedTableContainerProvider implements INamedContainerProvider {
        private final TileEntityAdvancedTable te;
        private final BlockPos pos;

        public AdvancedTableContainerProvider(TileEntityAdvancedTable te, BlockPos pos) {
            this.te = te;
            this.pos = pos;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("tile.eplus.advanced.table.name");
        }

        @Override
        public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
            EnchantmentLogicController logic = new EnchantmentLogicController(player, te.getLevel(), pos, te.getInventory(player));
            return new ContainerAdvancedTable(id, inv, logic);
        }
    }
}
