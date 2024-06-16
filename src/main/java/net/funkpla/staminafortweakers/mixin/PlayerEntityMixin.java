package net.funkpla.staminafortweakers.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaEntity;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StaminaEntity {

    @Unique
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    @Unique
    private boolean jumped;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract HungerManager getHungerManager();


    @Override
    public void update() {
        if (isSwimming()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting()) depleteStamina(config.depletionPerTickSprinting);
        else if (config.jumpingCostsStamina && hasJumped()) depleteStamina(config.depletionPerJump);
        else if (canRecover()) doRecovery();
        doExhaustion();
    }

    @Unique
    public StatusEffectInstance makeSlow(int amplifier) {
        return new StatusEffectInstance(StatusEffects.SLOWNESS, 3, amplifier, true, false);
    }

    @Unique
    public void doExhaustion() {
        double pct = getExhaustionPct();
        if (pct <= config.exhaustedPercentage) {
            if (config.exhaustionBlackout) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 40, 1, true, false));
            }
            addStatusEffect(makeSlow(4));
            setSprinting(false);
        } else if (pct <= config.windedPercentage) addStatusEffect(makeSlow(2));
        else if (pct <= config.fatiguedPercentage) addStatusEffect(makeSlow(1));
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
    public double getExhaustionPct() {
        return getStamina() / getMaxStamina() * 100;
    }

    @Unique
    public double getMaxStamina() {
        return getAttributeBaseValue(StaminaForTweakers.MAX_STAMINA);
    }

    @Unique
    public double getStamina() {
        return getAttributeBaseValue(StaminaForTweakers.STAMINA);
    }

    @Unique
    public void setStamina(double stamina) {
        getAttributeInstance(StaminaForTweakers.STAMINA).setBaseValue(stamina);
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
    }

    private boolean isStandingStill() {
        return ((horizontalSpeed - prevHorizontalSpeed) <= 0.1);
    }

    @Unique
    private boolean canRecover() {
        return (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileWalking || isStandingStill())
                && (config.recoverWhileAirborne || isOnGround())
                && (config.recoverUnderwater || !isSubmergedInWater());
    }

    @Unique
    private boolean isNotHungry() {
        return getHungerManager().getFoodLevel() >= 6;
    }


    /**
     * Track whether the entity jumped this tick. Set the flag in jump() and clear it at the beginning of every tick.
     */

    @Inject(method = "tick", at = @At("HEAD"))
    private void clearJumpedFlag(CallbackInfo ci) {
        jumped = false;
    }

    @Inject(method = "jump", at = @At("TAIL"))
    private void setJumpedFlag(CallbackInfo ci) {
        jumped = true;
    }

    /**
     * Inject into LivingEntity.jump() to block jumping according to config.
     */

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void blockJumping(CallbackInfo ci) {
        if (config.canJumpWhileExhausted) return;
        if (getExhaustionPct() <= config.exhaustedPercentage) ci.cancel();
    }


    private boolean hasJumped() {
        return jumped;
    }

}
