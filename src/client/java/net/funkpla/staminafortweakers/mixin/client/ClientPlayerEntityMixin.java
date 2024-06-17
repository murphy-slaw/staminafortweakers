package net.funkpla.staminafortweakers.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.funkpla.staminafortweakers.mixin.PlayerEntityMixin;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntityMixin {

    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void updateStamina(CallbackInfo ci) {
        if (isSwimming()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting()) depleteStamina(config.depletionPerTickSprinting);
        else if (config.jumpingCostsStamina && hasJumped()) depleteStamina(config.depletionPerJump);
        else if (isClimbing() && getVelocity().y > 0) depleteStamina(config.depletionPerTickSprinting);
        else if (canRecover()) doRecovery();
        doExhaustion();
    }


    @Unique
    public void doRecovery() {
        if (horizontalSpeed - prevHorizontalSpeed <= 0.01) {
            recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
        } else if (config.recoverWhileWalking ||
                (config.recoverWhileSneaking && isSneaking())) {
            recoverStamina(getBaseRecovery());
        }
    }

    public double getBaseRecovery() {
        if (config.formula == StaminaConfig.Formula.LOGARITHMIC) return calcLogRecovery();
        // Formula.LINEAR
        return config.recoveryPerTick;
    }

    public double calcLogRecovery() {
        return Math.log(Math.pow((getMaxStamina() - getStamina() + 1), (double) 1 / 3))
                / Math.log(3)
                * config.recoveryPerTick;
    }

    @Unique
    private boolean canRecover() {
        return (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileWalking || isStandingStill())
                && (config.recoverWhileAirborne || isOnGround())
                && (config.recoverUnderwater || !isSubmergedInWater());
    }

    @Unique
    public void recoverStamina(double recoveryAmount) {
        setStamina(getStamina() + recoveryAmount);
        if (getStamina() > getMaxStamina()) {
            setStamina(getMaxStamina());
        }
    }

    @Unique
    public void depleteStamina(float depletionAmount) {
        setStamina(getStamina() - depletionAmount);
        if (getStamina() < 0) setStamina(0);
    }

    private boolean isStandingStill() {
        return getVelocity().horizontalLength() == 0;
    }

    @Unique
    private boolean isNotHungry() {
        return getHungerManager().getFoodLevel() >= 6;
    }

    @Inject(
            method = "canSprint()Z",
            at = @At("HEAD"),
            cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        double stamina = getAttributeValue(StaminaForTweakers.STAMINA);
        double max_stamina = getAttributeValue(StaminaForTweakers.MAX_STAMINA);
        boolean isNotExhausted = ((stamina / max_stamina) * 100) <= config.exhaustedPercentage;
        cir.setReturnValue(
                isNotExhausted || this.hasVehicle() || (float) this.getHungerManager().getFoodLevel() > 6.0F || this.getAbilities().allowFlying
        );
    }

}