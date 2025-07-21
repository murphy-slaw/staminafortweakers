package net.funkpla.staminafortweakers.potion;

import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class TirelessnessPotion extends Potion {
    public TirelessnessPotion(int duration) {
        super(new MobEffectInstance(Services.REGISTRY.getTirelessnessEffect(), duration, 0));
    }
}
