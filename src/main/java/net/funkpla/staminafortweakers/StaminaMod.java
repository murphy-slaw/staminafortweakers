package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.Potions;
import net.funkpla.staminafortweakers.registry.SoundEvents;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StaminaMod implements ModInitializer {
    public static final String MOD_ID = "staminafortweakers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
        Attributes.register();
        StatusEffects.register();
        SoundEvents.register();
        Potions.register();

        /*
         * TOO MUCH COFFEE
         */

        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (player.hasEffect(StatusEffects.TIRELESSNESS)) {
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static ResourceLocation locate(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}