package net.funkpla.staminafortweakers.effect;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
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
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "B7793F99-A88F-44E1-B19E-C753E9ACED3F", -0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(CLIMB_SPEED, "2059DAB0-6417-46B1-AAA1-26BB473E773F", -0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, "11D1DE67-7351-499C-BF48-4836D6EEC8FF", -0.15f, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, "CFC553C2-5856-4BFC-8B30-892CFF036730", -0.1f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
