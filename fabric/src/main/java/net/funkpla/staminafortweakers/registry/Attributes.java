package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.attribute.AttributeHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class Attributes {
  public static final Holder<Attribute> STAMINA = registerAttribute("generic.stamina", AttributeHelper.createStamina());
  public static final Holder<Attribute> MAX_STAMINA = registerAttribute("generic.max_stamina", AttributeHelper.createMaxStamina());
  public static final Holder<Attribute> CLIMB_SPEED = registerAttribute("generic.climb_speed", AttributeHelper.createClimbSpeed());

  private static Holder<Attribute> registerAttribute(String name, Attribute attribute){
    return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Common.locate(name), attribute);
  }

  public static void register() {}
}
