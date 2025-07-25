package net.funkpla.staminafortweakers.packet.payload;

import net.funkpla.staminafortweakers.packet.C2SPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SwimPacketPayload(boolean swamUp) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SwimPacketPayload> TYPE = new CustomPacketPayload.Type<>(C2SPackets.SWIMMING_PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SwimPacketPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, SwimPacketPayload::swamUp, SwimPacketPayload::new);

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
