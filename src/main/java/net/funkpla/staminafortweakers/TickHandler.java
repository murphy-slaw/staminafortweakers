package net.funkpla.staminafortweakers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class TickHandler implements ServerTickEvents.EndWorldTick {

    public void onEndTick(ServerWorld server) {
        for (ServerPlayerEntity player : server.getPlayers()) {
            ((StaminaManager) player).update();
        }
    }
}