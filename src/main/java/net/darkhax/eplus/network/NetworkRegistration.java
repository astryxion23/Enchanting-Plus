package net.darkhax.eplus.network;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.payload.EnchantPayload;
import net.darkhax.eplus.network.payload.SliderUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;

public final class NetworkRegistration {

    public static void register() {
        PayloadTypeRegistry.playC2S().register(EnchantPayload.TYPE, EnchantPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(SliderUpdatePayload.TYPE, SliderUpdatePayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(EnchantPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();
                if (player != null && player.containerMenu instanceof ContainerAdvancedTable)
                    ((ContainerAdvancedTable) player.containerMenu).logic.enchantItem();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SliderUpdatePayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                if (payload.enchantmentKey() == null) return;
                ServerPlayer player = context.player();
                if (player == null) return;
                if (!(player.containerMenu instanceof ContainerAdvancedTable)) return;
                var reg = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
                Enchantment ench = reg.get(payload.enchantmentKey());
                if (ench != null)
                    ((ContainerAdvancedTable) player.containerMenu).logic.updateEnchantment(ench, payload.level());
            });
        });
    }
}
