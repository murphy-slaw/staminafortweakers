package net.funkpla.staminafortweakers.platform.services;

import net.minecraft.server.level.ServerPlayer;

public interface IPacketHelper {
    void sendMovementInputPacket(boolean hasMovementInput);
    void sendSwimPacket();
}

