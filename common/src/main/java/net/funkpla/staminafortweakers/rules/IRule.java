package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public interface IRule {
  boolean shouldEnable(StaminaConfig config);

  boolean test(ServerPlayer player);

  void doResult(StaminaConfig config, ServerPlayer player);

  default boolean execute(StaminaConfig config, ServerPlayer player) {
    boolean result = false;
    if (test(player)) {
      doResult(config, player);
      result = true;
    }
    return result;
  }
}
