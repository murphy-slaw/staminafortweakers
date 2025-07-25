package net.funkpla.staminafortweakers.packet.payload;

import io.netty.buffer.ByteBuf;
import net.funkpla.staminafortweakers.packet.C2SPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MovementPacketPayload(boolean moving) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MovementPacketPayload> TYPE = new CustomPacketPayload.Type<>(C2SPackets.MOVEMENT_INPUT_PACKET_ID);
    public static final StreamCodec<ByteBuf, MovementPacketPayload> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, MovementPacketPayload::moving, MovementPacketPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
