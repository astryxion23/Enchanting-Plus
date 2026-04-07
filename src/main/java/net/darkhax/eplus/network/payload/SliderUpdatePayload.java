package net.darkhax.eplus.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SliderUpdatePayload(Identifier enchantmentId, int level) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SliderUpdatePayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("eplus", "slider_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SliderUpdatePayload> CODEC =
            StreamCodec.composite(
                    Identifier.STREAM_CODEC,
                    SliderUpdatePayload::enchantmentId,
                    ByteBufCodecs.INT,
                    SliderUpdatePayload::level,
                    SliderUpdatePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
