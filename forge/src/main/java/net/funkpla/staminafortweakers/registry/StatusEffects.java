package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.effect.FatigueStatusEffect;
import net.funkpla.staminafortweakers.effect.TirelessnessStatusEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class StatusEffects {
  @ObjectHolder(registryName = "minecraft:mob_effect", value = "staminafortweakers:fatigue")
  public static final MobEffect FATIGUE = null;

  @ObjectHolder(registryName = "minecraft:mob_effect", value = "staminafortweakers:tirelessness")
  public static final MobEffect TIRELESSNESS = null;

  private static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MOD_ID);

  public static void registerStatusEffects() {
    MOB_EFFECTS.register("fatigue", FatigueStatusEffect::new);
    MOB_EFFECTS.register("tirelessness", TirelessnessStatusEffect::new);
    MOB_EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
