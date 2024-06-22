package net.funkpla.staminafortweakers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StaminaPotions {
    public static final Potion FATIGUE_POTION =
            Registry.register(Registries.POTION, new Identifier(StaminaForTweakers.MOD_ID, "fatigue_potion"),
                    new Potion(new StatusEffectInstance(StaminaForTweakers.FATIGUE, 3600, 2)));

    public static final Potion TIRELESSNESS_POTION =
            Registry.register(Registries.POTION, new Identifier(StaminaForTweakers.MOD_ID, "tirelessness_potion"),
                    new Potion(new StatusEffectInstance(StaminaForTweakers.TIRELESSNESS, 3600, 1)));

    public static void registerPotions() {

    }

    public static void registerPotionRecipes() {
        BrewingRecipeRegistry.registerPotionRecipe(Potions.THICK, Items.CLAY_BALL, StaminaPotions.FATIGUE_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.THICK, Items.COCOA_BEANS, StaminaPotions.TIRELESSNESS_POTION);
    }

}
