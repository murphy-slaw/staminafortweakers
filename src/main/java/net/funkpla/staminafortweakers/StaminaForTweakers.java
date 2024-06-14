package net.funkpla.staminafortweakers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StaminaForTweakers implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "staminafortweakers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static double ExhaustedPercentage = 0;

    public static EntityAttribute STAMINA = registerAttribute(
            "generic.stamina",
            100,
            0,
            1024
    );
    public static EntityAttribute MAX_STAMINA = registerAttribute(
            "generic.max_stamina",
            100,
            0,
            1024
    );

    private static EntityAttribute registerAttribute(final String name, double base, double min, double max) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setTracked(true);
        return Registry.register(Registries.ATTRIBUTE, new Identifier(MOD_ID, name), attribute);
    }


    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        TickHandler t = new TickHandler();
        ServerTickEvents.END_WORLD_TICK.register(t);
    }
}