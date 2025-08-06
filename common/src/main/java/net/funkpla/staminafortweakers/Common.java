package net.funkpla.staminafortweakers;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import lombok.Setter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.util.Utils;
import net.funkpla.staminafortweakers.config.EffectConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.rules.*;
import net.minecraft.resources.ResourceLocation;

public class Common {
  public static StaminaConfig config;
  private static Ruleset ruleset;
  @Setter private static boolean configDirty = false;

  public static void init() {
    Path configPath = Utils.getConfigFolder().resolve(Constants.MOD_ID + ".json5");
    boolean configIsNew = !Files.exists(configPath, LinkOption.NOFOLLOW_LINKS);
    AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
    config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    if (configIsNew) addDefaultEffects();
    buildRuleset();
  }

  public static void addDefaultEffects() {
    config.exhaustedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 4, true, true));
    config.exhaustedEffects.add(new EffectConfig("minecraft:darkness", 60, 0, true, false));
    config.exhaustedEffects.add(new EffectConfig("minecraft:mining_fatigue", 20, 1, true, true));
    config.windedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 2, true, true));
    config.fatiguedEffects.add(new EffectConfig("staminafortweakers:fatigue", 3, 0, true, true));
    AutoConfig.getConfigHolder(StaminaConfig.class).save();
  }

  public static ResourceLocation locate(String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }

  public static Ruleset getRuleset() {
    if (configDirty) {
      buildRuleset();
      setConfigDirty(false);
    }
    return ruleset;
  }

  public static void buildRuleset() {
    ruleset =
        new Ruleset(config)
            .tryAdd(new SwimmingExhausts())
            .tryAdd(new SprintingExhausts())
            .tryAdd(new JumpingExhausts())
            .tryAdd(new ClimbingExhausts())
            .tryAdd(new MiningExhausts())
            .tryAdd(new AttackingExhausts())
            .tryAdd(new SwingingWeaponExhausts())
            .tryAdd(new ShieldUseExhausts())
            .tryAdd(new GlidingExhausts())
            .build();
  }
}
