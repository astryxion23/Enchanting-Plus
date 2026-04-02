package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class MessageEnchant {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EnchantingPlus.CHANNEL_ENCHANT, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (player != null && player.containerMenu instanceof ContainerAdvancedTable) {
                    ((ContainerAdvancedTable) player.containerMenu).logic.enchantItem();
                }
            });
        });
    }

    public static void encode(FriendlyByteBuf buf) {
        // no payload
    }
}
