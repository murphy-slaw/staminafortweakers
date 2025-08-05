package net.funkpla.staminafortweakers.compat.bettercombat;

import net.bettercombat.api.client.BetterCombatClientEvents;
import net.funkpla.staminafortweakers.platform.Services;

public class BetterCombatCompat {
  public static void init() {
    if (Services.PLATFORM.isModLoaded("bettercombat")) {
      BetterCombatClientEvents.ATTACK_START.register(
          (player, hand) -> {
            if (!(player.isCreative() || player.isSpectator()))
              Services.PACKET.sendWeaponSwingPacket();
          });
    }
  }
}
