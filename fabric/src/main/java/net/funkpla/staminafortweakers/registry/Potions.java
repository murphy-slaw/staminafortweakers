package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaCommon;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class Potions {
    public static final Potion FATIGUE_POTION = registerPotion(StaminaCommon.locate("fatigue_potion"), new FatiguePotion(3600, 2));
    public static final Potion LONG_FATIGUE_POTION = registerPotion(StaminaCommon.locate("long_fatigue_potion"), new FatiguePotion(9600, 2));
    public static final Potion TIRELESSNESS_POTION = registerPotion(StaminaCommon.locate("tirelessness_potion"), new TirelessnessPotion(3600));
    public static final Potion LONG_TIRELESSNESS_POTION = registerPotion(StaminaCommon.locate("long_tirelessness_potion"), new TirelessnessPotion(9600));

    private static Potion registerPotion(ResourceLocation id, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, id, potion);
    }

    private static void registerPotionRecipes() {
        PotionBrewing.addMix(net.minecraft.world.item.alchemy.Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
        PotionBrewing.addMix(FATIGUE_POTION, Items.REDSTONE, LONG_FATIGUE_POTION);
        PotionBrewing.addMix(net.minecraft.world.item.alchemy.Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
        PotionBrewing.addMix(TIRELESSNESS_POTION, Items.REDSTONE, LONG_TIRELESSNESS_POTION);
    }

    public static void register() {
        registerPotionRecipes();
    }
}
