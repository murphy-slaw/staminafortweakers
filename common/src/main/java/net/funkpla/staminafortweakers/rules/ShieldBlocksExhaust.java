package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public class ShieldBlocksExhaust implements IRule {
  @Override
  public boolean shouldEnable(StaminaConfig config) {
    return config.depletionPerShieldBlock > 0;
  }

  @Override
  public boolean test(ServerPlayer player) {
    return ((Exhaustible) player).hasShieldBlocked();
  }

  @Override
  public void doResult(StaminaConfig config, ServerPlayer player) {
    ((Exhaustible) player).depleteStamina(config.depletionPerShieldBlock);
  }
}
