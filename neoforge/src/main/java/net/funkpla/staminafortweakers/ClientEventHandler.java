package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ClientEventHandler {
  @SubscribeEvent
  public static void handleLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
    ItemStack stack = event.getItemStack();
    Player player = event.getEntity();
    if (player.isCreative() || player.isSpectator()) return;
    if (!stack.isEmpty()) {
      if (stack.is(Constants.MELEE_WEAPON)) {
        Services.PACKET.sendWeaponSwingPacket();
      }
    }
  }
}
