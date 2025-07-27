package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.PacketHandler;
import net.minecraft.server.level.ServerPlayer;

public class ForgePacketHelper implements IPacketHelper {
  public void sendMovementInputPacket(boolean hasMovementInput) {
    PacketHandler.sendMovementInputPacket(hasMovementInput);
  }

  @Override
  public void sendSwimPacket() {
    PacketHandler.sendSwimPacket();
  }
}
