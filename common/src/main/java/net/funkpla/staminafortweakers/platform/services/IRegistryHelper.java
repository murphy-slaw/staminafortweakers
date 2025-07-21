package net.funkpla.staminafortweakers.platform.services;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IRegistryHelper {
  Attribute getWaterSpeedAttribute();

  Attribute getStaminaAttribute();

  Attribute getMaxStaminaAttribute();

  Attribute getClimbSpeedAttribute();

  MobEffect getTirelessnessEffect();

  MobEffect getFatigueEffect();

  Enchantment getTravelingEnchantment();

  Enchantment getUntiringEnchantment();
}
