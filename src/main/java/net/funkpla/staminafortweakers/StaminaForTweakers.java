package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.Holder;
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


    public static Holder<Attribute> STAMINA = registerAttribute(
            "generic.stamina",
            100,
            0,
            1024
    );
    public static Holder<Attribute> MAX_STAMINA = registerAttribute(
            "generic.max_stamina",
            100,
            0,
            1024
    );
    public static Holder<Attribute> CLIMB_SPEED = registerAttribute(
            "generic.climb_speed",
            0,
            -1,
            1
    );

    private static Holder<Attribute> registerAttribute(final String name, double base, double min, double max) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
        Attribute attribute = new RangedAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setSyncable(true);
        Registry.register(BuiltInRegistries.ATTRIBUTE, id, attribute);
        return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
    }

    public static final ResourceLocation BREATH_SCARED = ResourceLocation.parse("staminafortweakers:breath_scared");
    public static SoundEvent ENTITY_PLAYER_PANT = SoundEvent.createVariableRangeEvent(BREATH_SCARED);

    public static Holder<MobEffect> FATIGUE;
    public static Holder<MobEffect> TIRELESSNESS;
    public static Holder<Enchantment> TRAVELING_ENCHANTMENT;


    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
        FATIGUE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("staminafortweakers", "fatigue"),
                new FatigueStatusEffect()
        );
        TIRELESSNESS = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("staminafortweakers", "tirelessness"),
                new TirelessnessStatusEffect()
        );
        Registry.register(BuiltInRegistries.SOUND_EVENT, StaminaForTweakers.BREATH_SCARED, ENTITY_PLAYER_PANT);
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
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}