package net.funkpla.staminafortweakers;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class StaminaPotions {
    public static final Holder<Potion> FATIGUE_POTION =
            Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(StaminaForTweakers.MOD_ID, "fatigue_potion"),
                    new Potion(new MobEffectInstance(StaminaForTweakers.FATIGUE, 3600, 2)));

    public static final Holder<Potion> TIRELESSNESS_POTION =
            Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(StaminaForTweakers.MOD_ID, "tirelessness_potion"),
                    new Potion(new MobEffectInstance(StaminaForTweakers.TIRELESSNESS, 3600, 0)));

    public static void registerPotions() {

    }

    public static void registerPotionRecipes() {
        new PotionBrewing.Builder(FeatureFlagSet.of()).addMix(Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
        new PotionBrewing.Builder(FeatureFlagSet.of()).addMix(Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
    }

}
