package net.darkhax.eplus.network.messages;

import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageEnchant {

    public MessageEnchant() {}

    public static void encode(MessageEnchant msg, FriendlyByteBuf buf) {}

    public static MessageEnchant decode(FriendlyByteBuf buf) {
        return new MessageEnchant();
    }

    public static void handle(MessageEnchant msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null && ctx.get().getSender().containerMenu instanceof ContainerAdvancedTable)
                ((ContainerAdvancedTable) ctx.get().getSender().containerMenu).logic.enchantItem();
        });
        ctx.get().setPacketHandled(true);
    }
}
