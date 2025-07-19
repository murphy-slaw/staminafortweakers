package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.funkpla.staminafortweakers.packet.payload.ShieldAllowedPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class S2CSenders {
    public static void sendShieldAllowedPacket(ServerPlayer player, boolean allowed) {
        ServerPlayNetworking.send(player, new ShieldAllowedPacketPayload(allowed));
    }
}