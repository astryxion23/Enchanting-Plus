package net.darkhax.eplus.creativetab;

import net.darkhax.eplus.block.ModBlocks;
import net.darkhax.eplus.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabEPlus {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "eplus");

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("eplus", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.eplus"))
            .icon(() -> new ItemStack(ModBlocks.ADVANCED_TABLE.get()))
            .displayItems((params, output) -> {
                output.accept(new ItemStack(ModItems.ADVANCED_TABLE.get()));
                output.accept(new ItemStack(ModItems.TABLE_UPGRADE.get()));
                output.accept(new ItemStack(ModItems.DECORATIVE_BOOK.get()));
            })
            .build());
}
