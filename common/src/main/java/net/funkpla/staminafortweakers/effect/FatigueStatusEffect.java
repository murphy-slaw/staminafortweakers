package net.funkpla.staminafortweakers.effect;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FatigueStatusEffect extends MobEffect {
    public FatigueStatusEffect() {
        super(
                MobEffectCategory.HARMFUL,
                0xcf6dcf
        );
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Common.locate("fatigue.speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Services.REGISTRY.getClimbSpeedAttribute(), Common.locate("fatigue.climb_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Services.REGISTRY.getWaterSpeedAttribute(), Common.locate("fatigue.water_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, Common.locate("fatigue.attack_speed"), -0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
