package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
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
    public static EntityAttribute CLIMB_SPEED = registerAttribute(
            "generic.climb_speed",
            0,
            -1,
            1
    );

    private static EntityAttribute registerAttribute(final String name, double base, double min, double max) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setTracked(true);
        return Registry.register(Registries.ATTRIBUTE, new Identifier(MOD_ID, name), attribute);
    }


    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
    }
}