package net.funkpla.staminafortweakers.platform.services;

import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.NeoForgeMod;

public class NeoForgeRegistryHelper implements IRegistryHelper
{
    @Override
    public Holder<Attribute> getWaterSpeedAttribute() {
        return NeoForgeMod.SWIM_SPEED;
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
