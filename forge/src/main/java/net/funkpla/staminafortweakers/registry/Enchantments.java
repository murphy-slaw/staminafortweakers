package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Constants;
import net.funkpla.staminafortweakers.enchantment.TravelingEnchantment;
import net.funkpla.staminafortweakers.enchantment.UntiringEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class Enchantments {
  @ObjectHolder(registryName = "minecraft:enchantment", value = "staminafortweakers:untiring")
  public static final Enchantment UNTIRING = null;

  @ObjectHolder(registryName = "minecraft:enchantment", value = "staminafortweakers:traveling")
  public static final Enchantment TRAVELING = null;

  private static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MOD_ID);

  public static void registerEnchantments() {
    ENCHANTMENTS.register("untiring", UntiringEnchantment::new);
    ENCHANTMENTS.register("traveling", TravelingEnchantment::new);
    ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
