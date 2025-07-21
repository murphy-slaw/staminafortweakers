package net.funkpla.staminafortweakers.registry;

import net.funkpla.staminafortweakers.Common;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class SoundEvents {

  public static final String BREATH_SCARED = "breath_scared";

  public static final net.minecraft.sounds.SoundEvent ENTITY_PLAYER_PANT =
      SoundEvent.createVariableRangeEvent(Common.locate(BREATH_SCARED));

  private static void registerSoundEvent(String name, SoundEvent event) {
    Registry.register(BuiltInRegistries.SOUND_EVENT, name, event);
  }

  public static void register() {
    registerSoundEvent(BREATH_SCARED, ENTITY_PLAYER_PANT);
  }
}
