package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.config.StaminaConfigSerializer;
import net.funkpla.staminafortweakers.rules.Ruleset;
import net.minecraft.resources.ResourceLocation;

public class Common {
  private static RuleManager ruleManager;
  private static ConfigHolder<StaminaConfig> configHolder = null;

  public static ResourceLocation locate(String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }

  public static void initConfig() {
    AutoConfig.register(StaminaConfig.class, StaminaConfigSerializer::new);
    configHolder = AutoConfig.getConfigHolder(StaminaConfig.class);
    ruleManager = new RuleManager(configHolder);
    configHolder.registerLoadListener(ruleManager);
    configHolder.registerSaveListener(ruleManager);
    ruleManager.updateRules(getConfig());
  }

  public static StaminaConfig getConfig() {
    return configHolder.getConfig();
  }

  public static Ruleset getRules() {
    return ruleManager.getCurrentRules();
  }
}
