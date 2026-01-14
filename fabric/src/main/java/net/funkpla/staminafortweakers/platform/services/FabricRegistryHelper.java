package net.funkpla.staminafortweakers.platform.services;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.Enchantments;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.Enchantment;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public Attribute getWaterSpeedAttribute() {
    return AdditionalEntityAttributes.WATER_SPEED;
  }

  @Override
  public Attribute getStaminaAttribute() {
    return Attributes.STAMINA.value();
  }

  @Override
  public Attribute getMaxStaminaAttribute() {
    return Attributes.MAX_STAMINA.value();
  }

  @Override
  public Attribute getStaminaRecoveryAttribute() {
    return Attributes.STAMINA_RECOVERY_RATE.value();
  }

  @Override
  public Attribute getClimbSpeedAttribute() {
    return Attributes.CLIMB_SPEED.value();
  }

  @Override
  public MobEffect getTirelessnessEffect() {
    return StatusEffects.TIRELESSNESS;
  }

  @Override
  public MobEffect getFatigueEffect() {
    return StatusEffects.FATIGUE;
  }

  @Override
  public Enchantment getTravelingEnchantment() {
    return Enchantments.TRAVELING_ENCHANTMENT;
  }

  @Override
  public Enchantment getUntiringEnchantment() {
    return Enchantments.UNTIRING_ENCHANTMENT;
  }
}
