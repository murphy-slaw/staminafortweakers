package net.funkpla.staminafortweakers;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
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

    public static final Identifier BREATH_SCARED = new Identifier("staminafortweakers:breath_scared");
    public static SoundEvent ENTITY_PLAYER_PANT = SoundEvent.of(BREATH_SCARED);

    public static final StatusEffect FATIGUE = new FatigueStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "B7793F99-A88F-44E1-B19E-C753E9ACED3F", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(CLIMB_SPEED, "2059DAB0-6417-46B1-AAA1-26BB473E773F", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, "11D1DE67-7351-499C-BF48-4836D6EEC8FF", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("staminafortweakers", "fatigue"), FATIGUE);
        Registry.register(Registries.SOUND_EVENT, StaminaForTweakers.BREATH_SCARED, ENTITY_PLAYER_PANT);
    }
}