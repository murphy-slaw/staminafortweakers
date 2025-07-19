package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaCommon;
import net.funkpla.staminafortweakers.enchantment.TravelingEnchantment;
import net.funkpla.staminafortweakers.enchantment.UntiringEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

public class Enchantments {
    public static final Enchantment TRAVELING_ENCHANTMENT = new TravelingEnchantment();
    public static final Enchantment UNTIRING_ENCHANTMENT = new UntiringEnchantment();

    private static void registerEnchantment(String name, Enchantment enchantment) {
        Registry.register(BuiltInRegistries.ENCHANTMENT, StaminaCommon.locate(name), enchantment);
    }

    private static void registerEnchantments() {
        registerEnchantment("traveling", TRAVELING_ENCHANTMENT);
        registerEnchantment("untiring", UNTIRING_ENCHANTMENT);
    }

    public static void register() {
        registerEnchantments();
    }
}
