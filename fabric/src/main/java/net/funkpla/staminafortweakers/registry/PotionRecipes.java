package net.funkpla.staminafortweakers.registry;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import static net.funkpla.staminafortweakers.registry.Potions.*;

public class PotionRecipes {
    private static void registerPotionRecipes() {
        PotionBrewing.addMix(Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
        PotionBrewing.addMix(FATIGUE_POTION, Items.REDSTONE, LONG_FATIGUE_POTION);
        PotionBrewing.addMix(Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
        PotionBrewing.addMix(TIRELESSNESS_POTION, Items.REDSTONE, LONG_TIRELESSNESS_POTION);
    }

    public static void register() {
        registerPotionRecipes();
    }

}
