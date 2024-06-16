package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


public class StaminaForTweakers implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "staminafortweakers";

    public static RegistryEntry<EntityAttribute> STAMINA = registerAttribute(
            "generic.stamina",
            100,
            0,
            1024
    );
    public static RegistryEntry<EntityAttribute> MAX_STAMINA = registerAttribute(
            "generic.max_stamina",
            100,
            0,
            1024
    );

    private static RegistryEntry<EntityAttribute> registerAttribute(final String name, double base, double min, double max) {
        Identifier id = Identifier.of(MOD_ID, name);
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setTracked(true);
        Registry.register(Registries.ATTRIBUTE, id, attribute);
        return Registries.ATTRIBUTE.getEntry(attribute);
    }


    @Override
    public void onInitialize() {
        AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
        TickHandler t = new TickHandler();
        ServerTickEvents.END_WORLD_TICK.register(t);
    }
}