package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import static net.funkpla.staminafortweakers.StaminaMod.MOD_ID;

public class Attributes {
    public static final Holder<Attribute> STAMINA = registerAttribute(
            "generic.stamina",
            100,
            0,
            1024
    );
    public static final Holder<Attribute> MAX_STAMINA = registerAttribute(
            "generic.max_stamina",
            100,
            0,
            1024
    );
    public static final Holder<Attribute> CLIMB_SPEED = registerAttribute(
            "generic.climb_speed",
            0,
            -1,
            1
    );

    private static Holder<Attribute> registerAttribute(final String name, double base, double min, double max) {
        Attribute attribute = new RangedAttribute("attribute.name." + MOD_ID + '.' + name, base, min, max).setSyncable(true);
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, StaminaMod.locate(name), attribute);
    }

    public static void register() {
    }
}
