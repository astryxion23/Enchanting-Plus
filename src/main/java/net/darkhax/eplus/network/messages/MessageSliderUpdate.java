package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.util.EnchantData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class MessageSliderUpdate {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EnchantingPlus.CHANNEL_SLIDER, (server, player, handler, buf, responseSender) -> {
            ResourceLocation enchId = buf.readResourceLocation();
            int level = buf.readInt();
            server.execute(() -> {
                if (player == null || !(player.containerMenu instanceof ContainerAdvancedTable)) return;
                Enchantment ench = BuiltInRegistries.ENCHANTMENT.get(enchId);
                if (ench != null) {
                    ((ContainerAdvancedTable) player.containerMenu).logic.updateEnchantment(ench, level);
                }
            });
        });
    }

    public static void encode(FriendlyByteBuf buf, EnchantData data) {
        buf.writeResourceLocation(BuiltInRegistries.ENCHANTMENT.getKey(data.enchantment));
        buf.writeInt(data.enchantmentLevel);
    }
}
