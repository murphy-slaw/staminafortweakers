package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ClientEventHandler {
  @SubscribeEvent
  public static void handleLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
    ItemStack stack = event.getItemStack();
    Player player = event.getEntity();
    if (player.isCreative() || player.isSpectator()) return;
    if (!stack.isEmpty()) {
      if (stack.is(Constants.MELEE_WEAPON) && !Services.PLATFORM.isModLoaded("bettercombat")) {
        Services.PACKET.sendWeaponSwingPacket();
      }
    }
  }
}
