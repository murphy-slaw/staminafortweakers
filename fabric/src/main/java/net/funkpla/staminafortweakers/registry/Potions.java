package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.potion.FatiguePotion;
import net.funkpla.staminafortweakers.potion.TirelessnessPotion;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;

public class Potions {
    public static final Potion FATIGUE_POTION = registerPotion(Common.locate("fatigue_potion"), new FatiguePotion(3600, 2));
    public static final Potion LONG_FATIGUE_POTION = registerPotion(Common.locate("long_fatigue_potion"), new FatiguePotion(9600, 2));
    public static final Potion TIRELESSNESS_POTION = registerPotion(Common.locate("tirelessness_potion"), new TirelessnessPotion(3600));
    public static final Potion LONG_TIRELESSNESS_POTION = registerPotion(Common.locate("long_tirelessness_potion"), new TirelessnessPotion(9600));

    private static Potion registerPotion(ResourceLocation id, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, id, potion);
    }

    public static void register(){
    }

}
