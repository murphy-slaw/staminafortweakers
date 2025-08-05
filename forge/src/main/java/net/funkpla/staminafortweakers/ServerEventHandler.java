package net.funkpla.staminafortweakers;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEventHandler {

  @SubscribeEvent
  public static void handleAttack(AttackEntityEvent event) {
    Player player = event.getEntity();
    if (player.level().isClientSide || player.isCreative() || player.isSpectator()) return;
    ((Attacker) player).setAttacked(true);
  }

  @SubscribeEvent
  public static void handleBlockBreak(BlockEvent.BreakEvent event) {
    Player player = event.getPlayer();
    if (player.level().isClientSide || player.isCreative() || player.isSpectator()) return;
    ((Miner) player).depleteStaminaForBlockBreak();
  }

  @SubscribeEvent
  public static void handleUseShield(PlayerInteractEvent.RightClickItem event) {
    Player player = event.getEntity();
    if (player.isCreative() || player.isSpectator()) return;
    if (player.getItemInHand(event.getHand()).is(Constants.SHIELDS)) {
      if (((Exhaustible) player).isExhausted()) {
        event.setCanceled(true);
      }
    }
  }
}
