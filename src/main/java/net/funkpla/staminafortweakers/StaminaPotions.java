package net.funkpla.staminafortweakers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

public class StaminaPotions {
    public static final RegistryEntry<Potion> FATIGUE_POTION =
            Registry.registerReference(Registries.POTION, Identifier.of(StaminaForTweakers.MOD_ID, "fatigue_potion"),
                    new Potion(new StatusEffectInstance(StaminaForTweakers.FATIGUE, 3600, 2)));

    public static final RegistryEntry<Potion> TIRELESSNESS_POTION =
            Registry.registerReference(Registries.POTION, Identifier.of(StaminaForTweakers.MOD_ID, "tirelessness_potion"),
                    new Potion(new StatusEffectInstance(StaminaForTweakers.TIRELESSNESS, 3600, 0)));

    public static void registerPotions() {

    }

    public static void registerPotionRecipes() {
        new BrewingRecipeRegistry.Builder(FeatureSet.empty()).registerPotionRecipe(Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
        new BrewingRecipeRegistry.Builder(FeatureSet.empty()).registerPotionRecipe(Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
    }

}
