package net.funkpla.staminafortweakers.packet.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.funkpla.staminafortweakers.packet.C2SPackets;
import net.minecraft.network.FriendlyByteBuf;

public class C2SSenders {
    public static void sendSwimPacket() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(true);
        ClientPlayNetworking.send(C2SPackets.SWIMMING_PACKET_ID, buf);
    }
}
