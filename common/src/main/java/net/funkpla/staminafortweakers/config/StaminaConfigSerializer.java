package net.funkpla.staminafortweakers.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class StaminaConfigSerializer extends JanksonConfigSerializer<StaminaConfig> {
  public StaminaConfigSerializer(Config definition, Class<StaminaConfig> configClass) {
    super(definition, configClass);
  }

  @Override
  public StaminaConfig createDefault() {
    StaminaConfig config = super.createDefault();
    config.exhaustedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 4, true, true));
    config.exhaustedEffects.add(new EffectConfig("minecraft:darkness", 60, 0, true, false));
    config.exhaustedEffects.add(new EffectConfig("minecraft:mining_fatigue", 20, 1, true, true));
    config.windedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 2, true, true));
    config.fatiguedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 0, true, true));
    return config;
  }
}
