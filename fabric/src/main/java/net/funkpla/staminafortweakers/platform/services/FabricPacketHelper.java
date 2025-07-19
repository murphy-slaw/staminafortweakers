package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.packet.S2CPackets;
import net.funkpla.staminafortweakers.packet.S2CSenders;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketHelper implements IPacketHelper{
    public void sendShieldAllowedPacket(ServerPlayer player, boolean allowed){
        S2CSenders.sendShieldAllowedPacket(player,allowed);
    }
}
