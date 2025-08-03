package net.funkpla.staminafortweakers.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.funkpla.staminafortweakers.Attacker;
import net.funkpla.staminafortweakers.Swimmer;

public class C2SReceivers {
  public static void registerPackets() {
    ServerPlayNetworking.registerGlobalReceiver(
        C2SPackets.SWIMMING_PACKET_ID,
        ((server, player, handler, buf, responseSender) -> {
          boolean s = buf.readBoolean();
          ((Swimmer) player).setSwamUp(s);
        }));
    ServerPlayNetworking.registerGlobalReceiver(
        C2SPackets.MOVEMENT_INPUT_PACKET_ID,
        ((server, player, handler, buf, responseSender) -> {
          boolean s = buf.readBoolean();
          ((Swimmer) player).setHasMovementInput(s);
        }));
    ServerPlayNetworking.registerGlobalReceiver(
        C2SPackets.WEAPON_SWING_PACKET_ID,
        ((server, player, handler, buf, responseSender) -> {
          boolean s = buf.readBoolean();
          if (!player.swinging) ((Attacker) player).setSwungWeapon(s);
        }));
  }
}
