package net.funkpla.staminafortweakers.platform.services;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface IRegistryHelper {
  Holder<Attribute> getWaterSpeedAttribute();

  Holder<Attribute> getStaminaAttribute();

  Holder<Attribute> getMaxStaminaAttribute();

  Holder<Attribute> getStaminaRecoveryAttribute();

  Holder<Attribute> getClimbSpeedAttribute();

  Holder<MobEffect> getTirelessnessEffect();

  Holder<MobEffect> getFatigueEffect();
}
