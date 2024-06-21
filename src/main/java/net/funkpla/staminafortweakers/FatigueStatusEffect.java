package net.funkpla.staminafortweakers;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FatigueStatusEffect extends StatusEffect {
    public FatigueStatusEffect() {
        super(
                StatusEffectCategory.HARMFUL,
                0xcf6dcf
        );
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "B7793F99-A88F-44E1-B19E-C753E9ACED3F", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(StaminaForTweakers.CLIMB_SPEED, "2059DAB0-6417-46B1-AAA1-26BB473E773F", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, "11D1DE67-7351-499C-BF48-4836D6EEC8FF", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "CFC553C2-5856-4BFC-8B30-892CFF036730", -0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
