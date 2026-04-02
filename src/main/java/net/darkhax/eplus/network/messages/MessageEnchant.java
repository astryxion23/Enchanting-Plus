package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageEnchant {

    public MessageEnchant() {}

    public static void encode(MessageEnchant msg, PacketBuffer buf) {}

    public static MessageEnchant decode(PacketBuffer buf) {
        return new MessageEnchant();
    }

    public static void handle(MessageEnchant msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Container container = ctx.get().getSender().containerMenu;
            if (container instanceof ContainerAdvancedTable)
                ((ContainerAdvancedTable) container).logic.enchantItem();
        });
        ctx.get().setPacketHandled(true);
    }
}
