package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Potions {
  public static final DeferredRegister<Potion> POTIONS =
          DeferredRegister.create(BuiltInRegistries.POTION, Constants.MOD_ID);

  public static final Holder<Potion> FATIGUE_POTION = POTIONS.register("fatigue_potion", () -> new FatiguePotion(3600, 2));
  public static final Holder<Potion> LONG_FATIGUE_POTION = POTIONS.register("long_fatigue_potion", () -> new FatiguePotion(9600, 2));
  public static final Holder<Potion> TIRELESSNESS_POTION = POTIONS.register("tirelessness_potion", () -> new TirelessnessPotion(3600));
  public static final Holder<Potion> LONG_TIRELESSNESS_POTION = POTIONS.register("long_tirelessness_potion", () -> new TirelessnessPotion(9600));
}
