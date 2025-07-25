package net.funkpla.staminafortweakers.packet.payload;

import io.netty.buffer.ByteBuf;
import net.funkpla.staminafortweakers.packet.S2CPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ShieldAllowedPacketPayload(boolean shieldAllowed) implements CustomPacketPayload {
    public static final Type<ShieldAllowedPacketPayload> TYPE = new Type<>(S2CPackets.SHIELD_ALLOWED_PACKET_ID);
    public static final StreamCodec<ByteBuf, ShieldAllowedPacketPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ShieldAllowedPacketPayload::shieldAllowed, ShieldAllowedPacketPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
