package net.darkhax.eplus.item;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTableUpgrade extends Item {

    public ItemTableUpgrade(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (worldIn.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE) {
            worldIn.setBlock(pos, EnchantingPlus.blockAdvancedTable.defaultBlockState(), 3);

            if (!context.getPlayer().isCreative())
                context.getItemInHand().shrink(1);

            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return ActionResultType.PASS;
    }
}
