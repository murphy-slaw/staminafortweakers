package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class S2CSenders {
    public static void sendShieldAllowedPacket(ServerPlayer player, boolean allowed) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(allowed);
        ServerPlayNetworking.send(player, S2CPackets.SHIELD_ALLOWED_PACKET_ID,buf);
    }
}