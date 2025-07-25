package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.effect.FatigueStatusEffect;
import net.funkpla.staminafortweakers.effect.TirelessnessStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StatusEffects {
  public static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Constants.MOD_ID);

  public static final Holder<MobEffect> FATIGUE =
      MOB_EFFECTS.register("fatigue", FatigueStatusEffect::new);

  public static final Holder<MobEffect> TIRELESSNESS =
      MOB_EFFECTS.register("tirelessness", TirelessnessStatusEffect::new);
}
