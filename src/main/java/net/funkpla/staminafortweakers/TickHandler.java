package net.funkpla.staminafortweakers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TickHandler implements ServerTickEvents.EndWorldTick {

    public static final Logger LOGGER = LoggerFactory.getLogger(StaminaForTweakers.MOD_ID);
    private final StaminaConfig config;

    public TickHandler() {
        config = new StaminaConfig();
    }

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
                && (config.recoverWhenAirborne || player.isOnGround());

        if (player.isSwimming() || player.isSprinting())
            staminaAttr.setBaseValue(staminaAttr.getBaseValue() - config.depletionPerTick);
        else if (canRecover) {
            double recoveryAmount = Math.min(10, (Math.pow(maxStaminaAttr.getValue(), 2.0D)) / (Math.pow(staminaAttr.getValue(), 2.0D)));

            if (player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) <= 0) {
                LOGGER.debug("Standing still, bonus recovery;");
                staminaAttr.setBaseValue(staminaAttr.getBaseValue() + recoveryAmount * 2);
            } else if (config.recoverWhileWalking) {
                LOGGER.debug("Walking, normal recovery;");
                staminaAttr.setBaseValue(staminaAttr.getBaseValue() + recoveryAmount);
            }
            if (staminaAttr.getBaseValue() >= maxStamina) staminaAttr.setBaseValue(maxStamina);
        }

        double exhaustionPercentage = (staminaAttr.getValue() / maxStamina) * 100;

        if (exhaustionPercentage <= config.exhaustedPercentage) {
            player.addStatusEffect(makeSlow(4));
            player.setSprinting(false);
        } else if (exhaustionPercentage <= config.windedPercentage) player.addStatusEffect(makeSlow(2));
        else if (exhaustionPercentage <= config.fatiguedPercentage) player.addStatusEffect(makeSlow(1));
        else player.removeStatusEffect(StatusEffects.SLOWNESS);
    }
}
