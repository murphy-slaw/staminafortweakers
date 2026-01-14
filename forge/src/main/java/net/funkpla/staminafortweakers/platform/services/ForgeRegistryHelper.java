package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.Enchantments;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeMod;

public class ForgeRegistryHelper implements IRegistryHelper {

  @Override
  public Attribute getWaterSpeedAttribute() {
    return ForgeMod.SWIM_SPEED.get();
  }

  @Override
  public Attribute getStaminaAttribute() {
    return Attributes.STAMINA;
  }

  @Override
  public Attribute getMaxStaminaAttribute() {
    return Attributes.MAX_STAMINA;
  }

  @Override
  public Attribute getStaminaRecoveryAttribute() {
    return Attributes.STAMINA_RECOVERY_RATE;
  }

  @Override
  public Attribute getClimbSpeedAttribute() {
    return Attributes.CLIMB_SPEED;
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
    return Enchantments.TRAVELING;
  }

  @Override
  public Enchantment getUntiringEnchantment() {
    return Enchantments.UNTIRING;
  }
}
