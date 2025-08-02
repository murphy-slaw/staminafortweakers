package net.funkpla.staminafortweakers;

import static net.funkpla.staminafortweakers.Common.init;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.packet.C2SReceivers;
import net.funkpla.staminafortweakers.registry.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StaminaMod implements ModInitializer {

  @Override
  public void onInitialize() {
    init();
    Attributes.register();
    Enchantments.register();
    StatusEffects.register();
    SoundEvents.register();
    Potions.register();
    PotionRecipes.register();
    C2SReceivers.registerPackets();

    ServerLifecycleEvents.SERVER_STARTING.register(
        server -> {
          StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
          config.validatePostStart();
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
            if (stack.getItem() == Items.SHIELD) {
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
