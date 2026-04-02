package net.darkhax.eplus.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SliderUpdatePayload(ResourceLocation enchantmentKey, int level) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SliderUpdatePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("eplus", "slider_update"));

    public static final StreamCodec<FriendlyByteBuf, SliderUpdatePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    SliderUpdatePayload::enchantmentKey,
                    ByteBufCodecs.INT,
                    SliderUpdatePayload::level,
                    SliderUpdatePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
