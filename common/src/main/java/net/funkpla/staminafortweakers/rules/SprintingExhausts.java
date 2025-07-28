package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.server.level.ServerPlayer;

public class SprintingExhausts implements IRule {
  @Override
  public boolean shouldEnable(StaminaConfig config) {
    return config.depletionPerTickSprinting > 0;
  }

  @Override
  public boolean test(ServerPlayer player) {
    return ((Exhaustible) player).shouldSprintExhaust();
  }

  @Override
  public void doResult(StaminaConfig config, ServerPlayer player) {
    if (player instanceof Exhaustible exhaustible) {
      exhaustible.depleteStamina(
          config.depletionPerTickSprinting * exhaustible.getTravelingModifier());
      exhaustible.maybeDamageLeggings();
    }
  }
}
