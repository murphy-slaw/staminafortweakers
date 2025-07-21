package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.effect.FatigueStatusEffect;
import net.funkpla.staminafortweakers.effect.TirelessnessStatusEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class StatusEffects {
  public static final MobEffect FATIGUE = registerStatusEffect("fatigue", new FatigueStatusEffect());
  public static final MobEffect TIRELESSNESS = registerStatusEffect("tirelessness", new TirelessnessStatusEffect());

  private static MobEffect registerStatusEffect(String id, MobEffect effect) {
    return Registry.register(BuiltInRegistries.MOB_EFFECT, Common.locate(id), effect);
  }

  public static void register(){}
}
