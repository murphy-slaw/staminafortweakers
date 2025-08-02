package net.funkpla.staminafortweakers.packet.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.funkpla.staminafortweakers.packet.payload.MovementPacketPayload;
import net.funkpla.staminafortweakers.packet.payload.SwimPacketPayload;
import net.funkpla.staminafortweakers.packet.payload.WeaponSwingPacketPayload;

public class C2SSenders {
  public static void sendSwimPacket() {
    ClientPlayNetworking.send(new SwimPacketPayload(true));
  }

  public static void sendMovementInputPacket(boolean moving) {
    ClientPlayNetworking.send(new MovementPacketPayload(moving));
  }

  public static void sendWeaponSwingPacket() {
    ClientPlayNetworking.send(new WeaponSwingPacketPayload(true));
  }
}
