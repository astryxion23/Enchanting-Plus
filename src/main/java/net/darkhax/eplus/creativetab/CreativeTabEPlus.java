package net.darkhax.eplus.creativetab;

import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

public class CreativeTabEPlus {

    public static final ResourceKey<CreativeModeTab> TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("eplus", "eplus"));

    public static final CreativeModeTab TAB = register();

    private static CreativeModeTab register() {
        CreativeModeTab tab = FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.eplus"))
                .icon(() -> new ItemStack(ModBlocks.ADVANCED_TABLE))
                .displayItems((params, output) -> {
                    output.accept(new ItemStack(ModItems.ADVANCED_TABLE));
                    output.accept(new ItemStack(ModItems.TABLE_UPGRADE));
                    output.accept(new ItemStack(ModItems.DECORATIVE_BOOK));
                })
                .build();
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, tab);
    }
}
