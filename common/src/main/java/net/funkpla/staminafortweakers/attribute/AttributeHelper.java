package net.funkpla.staminafortweakers.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class AttributeHelper {
  private static Attribute createAttribute(final String name, double base, double min, double max) {
    return new RangedAttribute(name, base, min, max).setSyncable(true);
  }

  public static Attribute createClimbSpeed() {
    return createAttribute("generic.climb_speed", 0, -1, 1);
  }

  public static Attribute createStamina() {
    return createAttribute("generic.stamina", 100, 0, 1024);
  }

  public static Attribute createMaxStamina() {
    return createAttribute("generic.max_stamina", 100, 0, 1024);
  }
}
