package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public interface IRule {
  boolean shouldEnable(StaminaConfig config);

  boolean test(ServerPlayer player);

  void doResult(StaminaConfig config, ServerPlayer player);

  default void execute(StaminaConfig config, ServerPlayer player) {
    if (test(player)) {
      doResult(config, player);
    }
  }
}
