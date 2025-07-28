package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public class MiningExhausts implements IRule {

  @Override
  public boolean shouldEnable(StaminaConfig config) {
    return config.depletionPerMiningTick > 0;
  }

  @Override
  public boolean test(ServerPlayer player) {
    return ((Exhaustible) player).isMining();
  }

  @Override
  public void doResult(StaminaConfig config, ServerPlayer player) {
    if (player instanceof Exhaustible exhaustible) {
      exhaustible.depleteStamina(config.depletionPerMiningTick * exhaustible.getMiningModifier());
    }
  }
}
