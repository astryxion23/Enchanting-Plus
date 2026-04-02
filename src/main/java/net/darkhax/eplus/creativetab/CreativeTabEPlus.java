package net.darkhax.eplus.creativetab;

import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabEPlus {

    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation("eplus", "eplus"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.eplus"))
                    .icon(() -> new ItemStack(ModBlocks.ADVANCED_TABLE))
                    .displayItems((params, output) -> {
                        output.accept(new ItemStack(ModItems.ADVANCED_TABLE));
                        output.accept(new ItemStack(ModItems.TABLE_UPGRADE));
                        output.accept(new ItemStack(ModItems.DECORATIVE_BOOK));
                    })
                    .build());
}
