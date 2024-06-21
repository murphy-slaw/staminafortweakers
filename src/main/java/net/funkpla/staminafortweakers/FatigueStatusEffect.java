package net.funkpla.staminafortweakers;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

import static net.funkpla.staminafortweakers.StaminaForTweakers.MOD_ID;

public class FatigueStatusEffect extends StatusEffect {
    public FatigueStatusEffect() {
        super(
                StatusEffectCategory.HARMFUL,
                0xcf6dcf
        );
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(MOD_ID, "fatigue.speed"), -0.15f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(StaminaForTweakers.CLIMB_SPEED, Identifier.of(MOD_ID, "fatigue.climb_speed"), -0.15f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, Identifier.of(MOD_ID, "fatigue.water_speed"), -0.15f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
