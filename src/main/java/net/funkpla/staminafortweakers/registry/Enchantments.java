package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaMod;
import net.funkpla.staminafortweakers.enchantment.TravelingEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class Enchantments {
    public static Enchantment TRAVELING_ENCHANTMENT = new TravelingEnchantment();

    private Enchantment registerEnchantment(ResourceLocation id, Enchantment enchantment) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT, StaminaMod.locate("traveling"), new TravelingEnchantment());
    }

    public static void register() {
    }
}
