package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.PacketHandler;
import net.minecraft.server.level.ServerPlayer;

public class NeoForgePacketHelper implements IPacketHelper {
    @Override
    public void sendShieldAllowedPacket(ServerPlayer player, boolean allowed) {
        PacketHandler.sendShieldAllowedPacket(player, allowed);

    }

    @Override
    public void sendMovementInputPacket(boolean hasMovementInput) {
        PacketHandler.sendMovementPacket(hasMovementInput);

    }

    @Override
    public void sendSwimPacket() {
        PacketHandler.sendSwimPacket(true);
    }
}
