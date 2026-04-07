package net.darkhax.eplus.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record EnchantPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EnchantPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("eplus", "enchant"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EnchantPayload> CODEC = StreamCodec.unit(new EnchantPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
