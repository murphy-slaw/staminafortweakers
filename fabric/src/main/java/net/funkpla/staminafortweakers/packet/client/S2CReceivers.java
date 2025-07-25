package net.funkpla.staminafortweakers.packet.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.packet.payload.ShieldAllowedPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;


public class S2CReceivers {
    public static void registerS2CReceivers() {
        PayloadTypeRegistry.playS2C().register(ShieldAllowedPacketPayload.TYPE, ShieldAllowedPacketPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ShieldAllowedPacketPayload.TYPE, (payload, context) -> {
            ItemStack stack = Objects.requireNonNull(context.player()).getUseItem();
            if (!payload.shieldAllowed() && !stack.isEmpty() && stack.getItem() == Items.SHIELD) {
                context.player().stopUsingItem();
            }
            ((Exhaustible) Objects.requireNonNull(context.player())).setShieldAllowed(payload.shieldAllowed());
        });
    }
}
