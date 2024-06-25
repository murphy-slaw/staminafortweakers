package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaMod;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class Potions {
    public static final Holder<Potion> FATIGUE_POTION = registerPotion(StaminaMod.locate("fatigue_potion"), new FatiguePotion());
    public static final Holder<Potion> TIRELESSNESS_POTION = registerPotion(StaminaMod.locate("tirelessness_potion"), new TirelessnessPotion());

    private static Holder<Potion> registerPotion(ResourceLocation id, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, id, potion);
    }

    private static void registerPotionRecipes() {
        new PotionBrewing.Builder(FeatureFlagSet.of()).addMix(net.minecraft.world.item.alchemy.Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
        new PotionBrewing.Builder(FeatureFlagSet.of()).addMix(net.minecraft.world.item.alchemy.Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
    }

    public static void register() {
        registerPotionRecipes();
    }
}
