package net.funkpla.staminafortweakers;

import static net.funkpla.staminafortweakers.Common.init;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.packet.C2SReceivers;
import net.funkpla.staminafortweakers.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StaminaModFabric implements ModInitializer {

  private static boolean onClientPlayerPreAttack(
      Minecraft client, LocalPlayer player, int clickCount) {
    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    if (!(stack.isEmpty() || player.isCreative() || player.isSpectator())) {
      if (stack.is(ConventionalItemTags.MELEE_WEAPON_TOOLS)) {}
    }
    return false;
  }

  @Override
  public void onInitialize() {
    init();
    Attributes.register();
    StatusEffects.register();
    SoundEvents.register();
    Potions.register();
    C2SReceivers.registerPackets();

    ServerLifecycleEvents.SERVER_STARTING.register(
        server -> {
          StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
          config.validatePostStart();
        });

    ServerPlayerEvents.AFTER_RESPAWN.register(
        (oldPlayer, newPlayer, alive) -> {
          ((Exhaustible) newPlayer).handleRespawn();
        });

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
