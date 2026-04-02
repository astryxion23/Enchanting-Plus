package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.util.EnchantData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MessageSliderUpdate {

    public EnchantData updatedEnchant;

    public MessageSliderUpdate() {}

    public MessageSliderUpdate(EnchantData updatedEnchant) {
        this.updatedEnchant = updatedEnchant;
    }

    public static void encode(MessageSliderUpdate msg, PacketBuffer buf) {
        buf.writeResourceLocation(ForgeRegistries.ENCHANTMENTS.getKey(msg.updatedEnchant.enchantment));
        buf.writeInt(msg.updatedEnchant.enchantmentLevel);
    }

    public static MessageSliderUpdate decode(PacketBuffer buf) {
        Enchantment ench = ForgeRegistries.ENCHANTMENTS.getValue(buf.readResourceLocation());
        int level = buf.readInt();
        return new MessageSliderUpdate(ench != null ? new EnchantData(ench, level) : null);
    }

    public static void handle(MessageSliderUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.updatedEnchant == null) return;
            Container container = ctx.get().getSender().containerMenu;
            if (container instanceof ContainerAdvancedTable)
                ((ContainerAdvancedTable) container).logic.updateEnchantment(msg.updatedEnchant.enchantment, msg.updatedEnchant.enchantmentLevel);
        });
        ctx.get().setPacketHandled(true);
    }
}
