package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.attribute.AttributeHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Attributes {
  @ObjectHolder(registryName = "minecraft:attribute", value = "staminafortweakers:generic.stamina")
  public static final Attribute STAMINA = null;

  @ObjectHolder(
      registryName = "minecraft:attribute",
      value = "staminafortweakers:generic.max_stamina")
  public static final Attribute MAX_STAMINA = null;

  @ObjectHolder(
      registryName = "minecraft:attribute",
      value = "staminafortweakers:generic.stamina_recovery_rate")
  public static final Attribute STAMINA_RECOVERY_RATE = null;

  @ObjectHolder(
      registryName = "minecraft:attribute",
      value = "staminafortweakers:generic.climb_speed")
  public static final Attribute CLIMB_SPEED = null;

  private static final DeferredRegister<Attribute> ATTRIBUTES =
      DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Constants.MOD_ID);

  public static void registerAttributes() {
    ATTRIBUTES.register("generic.climb_speed", AttributeHelper::createClimbSpeed);
    ATTRIBUTES.register("generic.stamina", AttributeHelper::createStamina);
    ATTRIBUTES.register("generic.max_stamina", AttributeHelper::createMaxStamina);
    ATTRIBUTES.register("generic.stamina_recovery_rate", AttributeHelper::createStaminaRecovery);
    ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  @SubscribeEvent
  public static void addLivingEntityAttributes(EntityAttributeModificationEvent event) {
    event.getTypes().forEach(type -> event.add(type, CLIMB_SPEED));
    event.add(EntityType.PLAYER, STAMINA);
    event.add(EntityType.PLAYER, MAX_STAMINA);
    event.add(EntityType.PLAYER, STAMINA_RECOVERY_RATE);
  }
}
