package net.darkhax.eplus.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record EnchantPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EnchantPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("eplus", "enchant"));

    public static final StreamCodec<FriendlyByteBuf, EnchantPayload> STREAM_CODEC =
            StreamCodec.unit(new EnchantPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
