package net.funkpla.staminafortweakers.compat.combat_roll;

import net.combat_roll.api.event.ServerSideRollEvents;
import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.platform.Services;

public class CombatRollCompat {
  public static void init() {
    if (Services.PLATFORM.isModLoaded("combat_roll")) {
      ServerSideRollEvents.PLAYER_START_ROLLING.register(
          (player, velocity) -> {
            StaminaConfig config = Common.getConfig();
            if (!config.combatRollCompat) return;
            if (player.isCreative() || player.isSpectator()) return;
            ((Exhaustible) player).depleteStamina(config.depletionPerCombatRoll);
          });
    }
  }
}
