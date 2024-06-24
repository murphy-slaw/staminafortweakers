package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StaminaForTweakers implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "staminafortweakers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Attribute STAMINA = registerAttribute(
            "generic.stamina",
            100,
            0,
            1024
    );
    public static Attribute MAX_STAMINA = registerAttribute(
            "generic.max_stamina",
            100,
            0,
            1024
    );
    public static Attribute CLIMB_SPEED = registerAttribute(
            "generic.climb_speed",
            0,
            -1,
            1
    );

    private static Attribute registerAttribute(final String name, double base, double min, double max) {
        Attribute attribute = new RangedAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setSyncable(true);
        return Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(MOD_ID, name), attribute);
    }

    public static final MobEffect FATIGUE = new FatigueStatusEffect();
    public static final MobEffect TIRELESSNESS = new TirelessnessStatusEffect();
    public static final ResourceLocation BREATH_SCARED = resourceLocationOf("breath_scared");
    public static SoundEvent ENTITY_PLAYER_PANT = SoundEvent.createVariableRangeEvent(BREATH_SCARED);
    public static Enchantment TRAVELING_ENCHANTMENT = new TravelingEnchantment();

    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
        Registry.register(BuiltInRegistries.MOB_EFFECT, resourceLocationOf("fatigue"), FATIGUE);
        Registry.register(BuiltInRegistries.MOB_EFFECT, resourceLocationOf("tirelessness"), TIRELESSNESS);
        Registry.register(BuiltInRegistries.SOUND_EVENT, StaminaForTweakers.BREATH_SCARED, ENTITY_PLAYER_PANT);
        Registry.register(BuiltInRegistries.ENCHANTMENT, resourceLocationOf("traveling"), TRAVELING_ENCHANTMENT);
        StaminaPotions.registerPotions();
        StaminaPotions.registerPotionRecipes();

        /*
         * TOO MUCH COFFEE
         */

        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
            if (player.hasEffect(TIRELESSNESS)) {
                return Player.BedSleepingProblem.OTHER_PROBLEM;
            }
            return null;
        });
    }

    public static ResourceLocation resourceLocationOf(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}