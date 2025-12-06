package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.rules.*;
import net.minecraft.world.InteractionResult;

public class RuleManager
    implements ConfigSerializeEvent.Save<StaminaConfig>, ConfigSerializeEvent.Load<StaminaConfig> {
  private Ruleset ruleset;
  private final ConfigHolder<StaminaConfig> configHolder;

  public RuleManager(ConfigHolder<StaminaConfig> holder) {
    configHolder = holder;
  }

  @Override
  public InteractionResult onSave(
      ConfigHolder<StaminaConfig> configHolder, StaminaConfig staminaConfig) {
    updateRules(staminaConfig);
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult onLoad(
      ConfigHolder<StaminaConfig> configHolder, StaminaConfig staminaConfig) {
    updateRules(staminaConfig);
    return InteractionResult.SUCCESS;
  }

  public Ruleset getCurrentRules() {
    if (ruleset == null) {
      updateRules(configHolder.getConfig());
    }
    return ruleset;
  }

  public void updateRules(StaminaConfig config) {
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
