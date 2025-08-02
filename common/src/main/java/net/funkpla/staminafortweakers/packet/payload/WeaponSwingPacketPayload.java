package net.funkpla.staminafortweakers.packet.payload;

import net.funkpla.staminafortweakers.packet.C2SPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record WeaponSwingPacketPayload(boolean swung) implements CustomPacketPayload {
  public static final Type<WeaponSwingPacketPayload> TYPE =
      new Type<>(C2SPackets.WEAPON_SWING_PACKET_ID);
  public static final StreamCodec<RegistryFriendlyByteBuf, WeaponSwingPacketPayload> CODEC =
      StreamCodec.composite(
          ByteBufCodecs.BOOL, WeaponSwingPacketPayload::swung, WeaponSwingPacketPayload::new);

  @Override
  public @NotNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
