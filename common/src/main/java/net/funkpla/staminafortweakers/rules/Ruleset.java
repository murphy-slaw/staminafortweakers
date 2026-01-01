package net.funkpla.staminafortweakers.rules;

import com.google.common.collect.ImmutableList;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public class Ruleset {
  private ImmutableList<IRule> rules;
  private final ImmutableList.Builder<IRule> builder;
  private final StaminaConfig config;

  public Ruleset(StaminaConfig config) {
    this.config = config;
    this.builder = new ImmutableList.Builder<>();
  }

  public Ruleset tryAdd(IRule rule) {
    if (rule.shouldEnable(config)) builder.add(rule);
    return this;
  }

  public Ruleset build() {
    rules = builder.build();
    return this;
  }

  public void run(ServerPlayer player) {
    for (IRule rule : rules) {
      // Stop running rules after the first success
      if (rule.execute(config, player)) break;
    }
  }
}
