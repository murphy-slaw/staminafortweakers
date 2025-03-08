package net.funkpla.staminafortweakers.packet.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.packet.S2CPackets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class S2CReceivers {
    public static void registerS2CReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(S2CPackets.SHIELD_ALLOWED_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean allowed = buf.readBoolean();
            if (client.player != null) {
                ItemStack stack = client.player.getUseItem();
                if (!allowed && !stack.isEmpty() && stack.getItem() == Items.SHIELD) {
                    client.player.stopUsingItem();
                }
                ((Exhaustible) client.player).setShieldAllowed(allowed);
            }
        });
    }
}