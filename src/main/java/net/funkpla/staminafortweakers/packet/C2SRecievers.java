package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.funkpla.staminafortweakers.Swimmer;

public class C2SRecievers {
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(C2SPackets.SWIMMING_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            boolean s = buf.readBoolean();
            ((Swimmer) player).setSwamUp(s);
        }));
    }
}
