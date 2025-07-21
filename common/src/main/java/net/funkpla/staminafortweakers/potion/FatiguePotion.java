package net.funkpla.staminafortweakers.potion;

import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class FatiguePotion extends Potion {
    public FatiguePotion(int duration, int amplifier) {
        super(new MobEffectInstance(Services.REGISTRY.getFatigueEffect(), duration, amplifier));
    }
}
