package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.util.EnchantData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MessageSliderUpdate {

    public EnchantData updatedEnchant;

    public MessageSliderUpdate() {}

    public MessageSliderUpdate(EnchantData updatedEnchant) {
        this.updatedEnchant = updatedEnchant;
    }

    public static void encode(MessageSliderUpdate msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(msg.updatedEnchant.enchantment));
        buf.writeInt(msg.updatedEnchant.enchantmentLevel);
    }

    public static MessageSliderUpdate decode(FriendlyByteBuf buf) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(buf.readResourceLocation());
        int level = buf.readInt();
        return new MessageSliderUpdate(ench != null ? new EnchantData(ench, level) : null);
    }

    public static void handle(MessageSliderUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.updatedEnchant == null) return;
            if (ctx.get().getSender() != null && ctx.get().getSender().containerMenu instanceof ContainerAdvancedTable)
                ((ContainerAdvancedTable) ctx.get().getSender().containerMenu).logic.updateEnchantment(msg.updatedEnchant.enchantment, msg.updatedEnchant.enchantmentLevel);
        });
        ctx.get().setPacketHandled(true);
    }
}
