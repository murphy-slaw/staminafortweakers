package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.effect.FatigueStatusEffect;
import net.funkpla.staminafortweakers.effect.TirelessnessStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class StatusEffects {
  public static final Holder<MobEffect> FATIGUE = registerStatusEffect("fatigue", new FatigueStatusEffect());
  public static final Holder<MobEffect> TIRELESSNESS = registerStatusEffect("tirelessness", new TirelessnessStatusEffect());

  private static Holder<MobEffect> registerStatusEffect(String id, MobEffect effect) {
    return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Common.locate(id), effect);
  }

  public static void register(){}
}
