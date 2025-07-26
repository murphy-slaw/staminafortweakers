package net.funkpla.staminafortweakers.registry;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

public class Potions {

    public static final Holder<Potion> FATIGUE_POTION = registerPotion(Common.locate("fatigue_potion"), new FatiguePotion(3600, 2));
    public static final Holder<Potion> LONG_FATIGUE_POTION = registerPotion(Common.locate("long_fatigue_potion"), new FatiguePotion(9600, 2));
    public static final Holder<Potion> TIRELESSNESS_POTION = registerPotion(Common.locate("tirelessness_potion"), new TirelessnessPotion(3600));
    public static final Holder<Potion> LONG_TIRELESSNESS_POTION = registerPotion(Common.locate( "long_tirelessness_potion"), new TirelessnessPotion(9600));

    private static Holder<Potion> registerPotion(ResourceLocation id, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, id, potion);
    }

    private static void registerPotionRecipes() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(net.minecraft.world.item.alchemy.Potions.THICK, Ingredient.of(Items.CLAY_BALL), FATIGUE_POTION);
            builder.registerPotionRecipe(FATIGUE_POTION, Ingredient.of(Items.REDSTONE), LONG_FATIGUE_POTION);
            builder.registerPotionRecipe(net.minecraft.world.item.alchemy.Potions.THICK, Ingredient.of(Items.COCOA_BEANS), TIRELESSNESS_POTION);
            builder.registerPotionRecipe(TIRELESSNESS_POTION, Ingredient.of(Items.REDSTONE), LONG_TIRELESSNESS_POTION);
        });
    }

    public static void register() {
        registerPotionRecipes();
    }
}
