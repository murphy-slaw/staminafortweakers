package net.funkpla.staminafortweakers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.funkpla.staminafortweakers.packet.C2SReceivers;
import net.funkpla.staminafortweakers.registry.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StaminaModFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    Attributes.register();
    StatusEffects.register();
    SoundEvents.register();
    Potions.register();
    C2SReceivers.registerPackets();
    Common.initConfig();

    ServerPlayerEvents.AFTER_RESPAWN.register(
        (oldPlayer, newPlayer, alive) -> ((Exhaustible) newPlayer).handleRespawn());

    PlayerBlockBreakEvents.AFTER.register(
        (level, player, pos, state, entity) -> {
          if (level.isClientSide() || player.isCreative() || player.isSpectator()) return;
          ((Miner) player).depleteStaminaForBlockBreak();
        });

    AttackEntityCallback.EVENT.register(
        (player, level, hand, entity, hitResult) -> {
          if (!(level.isClientSide() || player.isCreative() || player.isSpectator())) {
            ((Attacker) player).setAttacked(true);
          }
          return InteractionResult.PASS;
        });

    UseItemCallback.EVENT.register(
        (player, level, hand) -> {
          ItemStack stack = player.getItemInHand(hand);
          if (!(player.isCreative() || player.isSpectator())) {
            if (stack.is(Constants.SHIELDS)) {
              if (((Exhaustible) player).isExhausted()) return InteractionResultHolder.fail(stack);
            }
          }
          return InteractionResultHolder.pass(stack);
        });

    /*
     * TOO MUCH COFFEE
     */

    EntitySleepEvents.ALLOW_SLEEPING.register(
        (player, sleepingPos) -> {
          if (player.hasEffect(StatusEffects.TIRELESSNESS)) {
            return Player.BedSleepingProblem.OTHER_PROBLEM;
          }
          return null;
        });
  }
}
