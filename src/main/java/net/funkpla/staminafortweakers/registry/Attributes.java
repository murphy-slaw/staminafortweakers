package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class Attributes {
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
        Attribute attribute = new RangedAttribute("attribute.name." + StaminaMod.MOD_ID + '.' + name, base, min, max).setSyncable(true);
        return Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(StaminaMod.MOD_ID, name), attribute);
    }

    public static void register() {
    }
}
