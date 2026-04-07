package net.darkhax.eplus.network;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.payload.EnchantPayload;
import net.darkhax.eplus.network.payload.SliderUpdatePayload;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class NetworkRegistration {

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(EnchantPayload.TYPE, EnchantPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SliderUpdatePayload.TYPE, SliderUpdatePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(EnchantPayload.TYPE, NetworkRegistration::handleEnchant);
        ServerPlayNetworking.registerGlobalReceiver(SliderUpdatePayload.TYPE, NetworkRegistration::handleSliderUpdate);
    }

    private static void handleEnchant(EnchantPayload payload, ServerPlayNetworking.Context context) {
        if (context.player().containerMenu instanceof ContainerAdvancedTable)
            ((ContainerAdvancedTable) context.player().containerMenu).logic.enchantItem();
    }

    private static void handleSliderUpdate(SliderUpdatePayload payload, ServerPlayNetworking.Context context) {
        if (payload.enchantmentId() == null) return;
        if (!(context.player().containerMenu instanceof ContainerAdvancedTable)) return;
        Registry<Enchantment> reg = context.player().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, payload.enchantmentId());
        Enchantment ench = reg.getOptional(key).orElse(null);
        if (ench != null)
            ((ContainerAdvancedTable) context.player().containerMenu).logic.updateEnchantment(ench, payload.level());
    }
}
