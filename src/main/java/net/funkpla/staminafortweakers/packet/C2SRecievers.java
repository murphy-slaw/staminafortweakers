package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.funkpla.staminafortweakers.Swimmer;
import net.funkpla.staminafortweakers.packet.payload.SwimPacketPayload;

public class C2SRecievers {
    public static void registerPackets() {
        PayloadTypeRegistry.playC2S().register(SwimPacketPayload.TYPE, SwimPacketPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SwimPacketPayload.TYPE, ((payload, context) -> ((Swimmer) context.player()).setSwamUp(payload.swamUp())));
    }
}
