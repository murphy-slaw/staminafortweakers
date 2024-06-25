package net.funkpla.staminafortweakers.potion;

import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class FatiguePotion extends Potion {
    public FatiguePotion() {
        super(new MobEffectInstance(StatusEffects.FATIGUE, 3600, 2));
    }
}