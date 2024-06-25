package net.funkpla.staminafortweakers.effect;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.funkpla.staminafortweakers.StaminaMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.funkpla.staminafortweakers.registry.Attributes.CLIMB_SPEED;


public class FatigueStatusEffect extends MobEffect {
    public FatigueStatusEffect() {
        super(
                MobEffectCategory.HARMFUL,
                0xcf6dcf
        );
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, StaminaMod.locate("fatigue.speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(CLIMB_SPEED, StaminaMod.locate("fatigue.climb_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, StaminaMod.locate("fatigue.water_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, StaminaMod.locate("fatigue.attack_speed"), -0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
