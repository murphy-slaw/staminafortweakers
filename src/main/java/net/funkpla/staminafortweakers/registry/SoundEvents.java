package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.StaminaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundEvents {
    public static final ResourceLocation BREATH_SCARED = StaminaMod.locate("breath_scared");
    public static net.minecraft.sounds.SoundEvent ENTITY_PLAYER_PANT = registerSoundEvent(
            BREATH_SCARED,
            net.minecraft.sounds.SoundEvent.createVariableRangeEvent(BREATH_SCARED));

    private static SoundEvent registerSoundEvent(ResourceLocation id, SoundEvent event) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, event);
    }

    public static void register() {
    }
}
