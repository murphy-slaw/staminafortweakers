package net.funkpla.staminafortweakers.packet.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.packet.S2CPackets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;


public class S2CRecievers {
    public static void registerC2SReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(S2CPackets.SHIELD_ALLOWED_PACKET_ID, (client, handler, buf,
                                                                                          responseSender) -> {
            boolean allowed = buf.readBoolean();
            ItemStack stack = Objects.requireNonNull(client.player).getUseItem();
            if (!allowed && !stack.isEmpty() && stack.getItem() == Items.SHIELD){
                client.player.stopUsingItem();
            }
            ((Exhaustible) Objects.requireNonNull(client.player)).setShieldAllowed(allowed);
        });
    }
}
