package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.packet.C2SSenders;
import net.funkpla.staminafortweakers.packet.S2CSenders;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketHelper implements IPacketHelper{
    @Override
    public void sendShieldAllowedPacket(ServerPlayer player, boolean allowed){
        S2CSenders.sendShieldAllowedPacket(player,allowed);
    }

    @Override
    public void sendMovementInputPacket(boolean hasMovementInput) {
        C2SSenders.sendMovementInputPacket(hasMovementInput);
    }

    @Override
    public void sendSwimPacket(){
        C2SSenders.sendSwimPacket();
    }
}
