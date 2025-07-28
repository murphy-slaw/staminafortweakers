package net.funkpla.staminafortweakers;

import lombok.Setter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.rules.*;
import net.minecraft.resources.ResourceLocation;

public class Common {
  public static StaminaConfig config;
  private static Ruleset ruleset;
  @Setter private static boolean configDirty = false;

  public static void init() {
    AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
    config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    buildRuleset();
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
            .tryAdd(new ShieldUseExhausts())
            .tryAdd(new GlidingExhausts())
            .build();
  }
}
