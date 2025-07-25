package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.attribute.AttributeHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Attributes {
  public static final DeferredRegister<Attribute> ATTRIBUTES =
      DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Constants.MOD_ID);

  public static final Holder<Attribute> STAMINA =
      ATTRIBUTES.register("generic.stamina", AttributeHelper::createStamina);

  public static final Holder<Attribute> MAX_STAMINA =
      ATTRIBUTES.register("generic.max_stamina", AttributeHelper::createMaxStamina);

  public static final Holder<Attribute> CLIMB_SPEED =
      ATTRIBUTES.register("generic.climb_speed", AttributeHelper::createClimbSpeed);


  @SubscribeEvent
  public static void addLivingEntityAttributes(EntityAttributeModificationEvent event) {
    event.getTypes().forEach(type -> event.add(type, CLIMB_SPEED));
    event.add(EntityType.PLAYER, STAMINA);
    event.add(EntityType.PLAYER, MAX_STAMINA);
  }
}
