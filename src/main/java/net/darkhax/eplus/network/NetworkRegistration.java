package net.darkhax.eplus.network;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.payload.EnchantPayload;
import net.darkhax.eplus.network.payload.SliderUpdatePayload;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class NetworkRegistration {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(EnchantPayload.TYPE, EnchantPayload.STREAM_CODEC, NetworkRegistration::handleEnchant);
        registrar.playToServer(SliderUpdatePayload.TYPE, SliderUpdatePayload.STREAM_CODEC, NetworkRegistration::handleSliderUpdate);
    }

    private static void handleEnchant(EnchantPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null && context.player().containerMenu instanceof ContainerAdvancedTable)
                ((ContainerAdvancedTable) context.player().containerMenu).logic.enchantItem();
        });
    }

    private static void handleSliderUpdate(SliderUpdatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.enchantmentId() == null) return;
            if (context.player() == null) return;
            if (!(context.player().containerMenu instanceof ContainerAdvancedTable)) return;
            Registry<Enchantment> reg = context.player().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Enchantment ench = reg.getOptional(payload.enchantmentId()).orElse(null);
            if (ench != null)
                ((ContainerAdvancedTable) context.player().containerMenu).logic.updateEnchantment(ench, payload.level());
        });
    }
}
