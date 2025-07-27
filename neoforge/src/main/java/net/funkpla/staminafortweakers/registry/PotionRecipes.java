package net.funkpla.staminafortweakers.registry;

import static net.funkpla.staminafortweakers.registry.Potions.*;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

public class PotionRecipes {
  @SubscribeEvent
  public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
    // Gets the builder to add recipes to
    PotionBrewing.Builder builder = event.getBuilder();
    builder.addMix(Potions.THICK, Items.CLAY_BALL, FATIGUE_POTION);
    builder.addMix(FATIGUE_POTION, Items.REDSTONE, LONG_FATIGUE_POTION);
    builder.addMix(Potions.THICK, Items.COCOA_BEANS, TIRELESSNESS_POTION);
    builder.addMix(TIRELESSNESS_POTION, Items.REDSTONE, LONG_TIRELESSNESS_POTION);
  }
}
