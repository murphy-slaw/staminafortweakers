package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class Potions {
  @ObjectHolder(registryName = "minecraft:potion", value = "staminafortweakers:fatigue_potion")
  public static final Potion FATIGUE_POTION = null;

  @ObjectHolder(registryName = "minecraft:potion", value = "staminafortweakers:long_fatigue_potion")
  public static final Potion LONG_FATIGUE_POTION = null;

  @ObjectHolder(registryName = "minecraft:potion", value = "staminafortweakers:tirelessness_potion")
  public static final Potion TIRELESSNESS_POTION = null;

  @ObjectHolder(
      registryName = "minecraft:potion",
      value = "staminafortweakers:long_tirelessness_potion")
  public static final Potion LONG_TIRELESSNESS_POTION = null;

  private static final DeferredRegister<Potion> POTIONS =
      DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MOD_ID);

  public static void registerPotions() {
    POTIONS.register("fatigue_potion", () -> new FatiguePotion(3600, 2));
    POTIONS.register("long_fatigue_potion", () -> new FatiguePotion(9600, 2));
    POTIONS.register("tirelessness_potion", () -> new TirelessnessPotion(3600));
    POTIONS.register("long_tirelessness_potion", () -> new TirelessnessPotion(9600));
    POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
