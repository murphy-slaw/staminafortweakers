package net.funkpla.staminafortweakers;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class TirelessnessStatusEffect extends StatusEffect {
    public TirelessnessStatusEffect() {
        super(
                StatusEffectCategory.BENEFICIAL,
                0xFFFF7657
        );
    }
}
