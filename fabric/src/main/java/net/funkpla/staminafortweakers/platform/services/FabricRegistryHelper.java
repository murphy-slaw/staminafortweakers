package net.funkpla.staminafortweakers.platform.services;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public Holder<Attribute> getWaterSpeedAttribute() {
    return AdditionalEntityAttributes.WATER_SPEED;
  }

  @Override
  public Holder<Attribute> getStaminaAttribute() {
    return Attributes.STAMINA;
  }

  @Override
  public Holder<Attribute> getMaxStaminaAttribute() {
    return Attributes.MAX_STAMINA;
  }

  @Override
  public Holder<Attribute> getClimbSpeedAttribute() {
    return Attributes.CLIMB_SPEED;
  }

  @Override
  public Holder<MobEffect> getTirelessnessEffect() {
    return StatusEffects.TIRELESSNESS;
  }

  @Override
  public Holder<MobEffect> getFatigueEffect() {
    return StatusEffects.FATIGUE;
  }
}
