package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.PacketHandler;

public class NeoForgePacketHelper implements IPacketHelper {
  @Override
  public void sendMovementInputPacket(boolean hasMovementInput) {
    PacketHandler.sendMovementPacket(hasMovementInput);
  }

  @Override
  public void sendSwimPacket() {
    PacketHandler.sendSwimPacket(true);
  }

  @Override
  public void sendWeaponSwingPacket() {
    PacketHandler.sendWeaponSwingPacket(true);
  }
}
