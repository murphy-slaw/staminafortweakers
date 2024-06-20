package net.funkpla.staminafortweakers;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FatigueStatusEffect extends StatusEffect {
    public FatigueStatusEffect() {
        super(
                StatusEffectCategory.HARMFUL,
                0xcf6dcf
        );
    }
}
