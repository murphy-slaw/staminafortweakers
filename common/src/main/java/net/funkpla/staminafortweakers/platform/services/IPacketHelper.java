package net.funkpla.staminafortweakers.platform.services;

public interface IPacketHelper {
  void sendMovementInputPacket(boolean hasMovementInput);

  void sendSwimPacket();

  void sendWeaponSwingPacket();
}
