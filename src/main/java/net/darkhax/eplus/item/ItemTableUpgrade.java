package net.darkhax.eplus.item;

import net.darkhax.eplus.EnchantingPlus;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ItemTableUpgrade extends Item {

    public ItemTableUpgrade(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (worldIn.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE) {
            worldIn.setBlock(pos, EnchantingPlus.blockAdvancedTable.defaultBlockState(), 3);

            if (!context.getPlayer().isCreative())
                context.getItemInHand().shrink(1);

            return worldIn.isClientSide() ? InteractionResult.CONSUME : InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }
}
