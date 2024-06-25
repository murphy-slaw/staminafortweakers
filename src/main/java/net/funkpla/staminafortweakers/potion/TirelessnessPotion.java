package net.funkpla.staminafortweakers.potion;

import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class TirelessnessPotion extends Potion {
    public TirelessnessPotion() {
        super(new MobEffectInstance(StatusEffects.TIRELESSNESS, 3600, 0));
    }
}
