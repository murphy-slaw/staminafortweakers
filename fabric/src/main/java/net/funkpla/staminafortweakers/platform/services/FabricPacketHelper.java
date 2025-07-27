package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.packet.C2SSenders;

public class FabricPacketHelper implements IPacketHelper{
    @Override
    public void sendMovementInputPacket(boolean hasMovementInput) {
        C2SSenders.sendMovementInputPacket(hasMovementInput);
    }

    @Override
    public void sendSwimPacket(){
        C2SSenders.sendSwimPacket();
    }
}
