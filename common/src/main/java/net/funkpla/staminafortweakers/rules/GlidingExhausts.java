package net.funkpla.staminafortweakers.rules;

import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.compat.vc_gliders.VCGlidersCompat;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.server.level.ServerPlayer;

public class GlidingExhausts implements IRule {
  @Override
  public boolean shouldEnable(StaminaConfig config) {
    return Services.PLATFORM.isModLoaded("vc_gliders") && config.depletionPerGliderTick > 0;
  }

  @Override
  public boolean test(ServerPlayer player) {
    return Services.PLATFORM.isModLoaded("vc_gliders") && VCGlidersCompat.isGliding(player);
  }

  @Override
  public void doResult(StaminaConfig config, ServerPlayer player) {
    ((Exhaustible) player).depleteStamina((config.depletionPerGliderTick));
  }
}
