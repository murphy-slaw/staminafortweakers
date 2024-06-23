package net.funkpla.staminafortweakers;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.funkpla.staminafortweakers.StaminaForTweakers.MOD_ID;

public class FatigueStatusEffect extends MobEffect {
    public FatigueStatusEffect() {
        super(
                MobEffectCategory.HARMFUL,
                0xcf6dcf
        );
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(MOD_ID, "fatigue.speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(StaminaForTweakers.CLIMB_SPEED, ResourceLocation.fromNamespaceAndPath(MOD_ID, "fatigue.climb_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, ResourceLocation.fromNamespaceAndPath(MOD_ID, "fatigue.water_speed"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(MOD_ID, "fatigue.attack_speed"), -0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
