package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class TickHandler implements ServerTickEvents.EndWorldTick {

    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    public void onEndTick(ServerWorld server) {
        server.getPlayers().forEach(this::handleStamina);
    }

    private static StatusEffectInstance makeSlow(int amplifier) {
        return new StatusEffectInstance(StatusEffects.SLOWNESS, 3, amplifier, true, false);
    }

    private void handleStamina(ServerPlayerEntity player) {
        EntityAttributeInstance staminaAttr = player.getAttributeInstance(StaminaForTweakers.STAMINA);
        if (staminaAttr == null) return;
        EntityAttributeInstance maxStaminaAttr = player.getAttributeInstance(StaminaForTweakers.MAX_STAMINA);
        if (maxStaminaAttr == null) return;
        double maxStamina = maxStaminaAttr.getValue();

        boolean notHungry = player.getHungerManager().getFoodLevel() >= 6;
        boolean canRecover = (config.recoverWhenHungry || notHungry)
                && (config.recoverWhileAirborne || player.isOnGround())
                && (config.recoverUnderwater || !player.isSubmergedInWater());

        if (player.isSwimming()
                || player.isSprinting()
                || (config.jumpingCostsStamina && ((Jumper) player).hasJumped())
        ) {
            staminaAttr.setBaseValue(staminaAttr.getBaseValue() - config.depletionPerTick);
        } else if (canRecover) {
            double recoveryAmount = config.recoveryPerTick;
            double moved = player.horizontalSpeed - player.prevHorizontalSpeed;
            if (moved <= 0.01) {
                staminaAttr.setBaseValue(staminaAttr.getBaseValue() + recoveryAmount * 2);
            } else if (config.recoverWhileWalking ||
                    (config.recoverWhileSneaking && player.isSneaking())) {
                staminaAttr.setBaseValue(staminaAttr.getBaseValue() + recoveryAmount);
            }
            if (staminaAttr.getBaseValue() >= maxStamina) staminaAttr.setBaseValue(maxStamina);
        }

        double exhaustionPercentage = (staminaAttr.getValue() / maxStamina) * 100;

        if (exhaustionPercentage <= config.exhaustedPercentage) {
            if (config.exhaustionSucksBad) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 3, 1, true, false));
            }
            player.addStatusEffect(makeSlow(4));
            player.addStatusEffect(makeSlow(4));
            player.setSprinting(false);
        } else if (exhaustionPercentage <= config.windedPercentage) player.addStatusEffect(makeSlow(2));
        else if (exhaustionPercentage <= config.fatiguedPercentage) player.addStatusEffect(makeSlow(1));
        else player.removeStatusEffect(StatusEffects.SLOWNESS);
    }
}
