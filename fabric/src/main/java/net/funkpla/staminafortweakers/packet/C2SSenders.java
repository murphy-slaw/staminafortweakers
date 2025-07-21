package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class C2SSenders {
    public static void sendSwimPacket() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(true);
        ClientPlayNetworking.send(C2SPackets.SWIMMING_PACKET_ID, buf);
    }
    public static void sendMovementInputPacket(boolean moving) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(moving);
        ClientPlayNetworking.send(C2SPackets.MOVEMENT_INPUT_PACKET_ID, buf);
    }
}