package net.funkpla.staminafortweakers.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaEntity;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements StaminaEntity {
    @Unique
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    @Unique
    private boolean jumped;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Inject into LivingEntity.createLivingAttributes to add STAMINA and MAX_STAMINA attributes.
     */

    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1,
            allow = 1,
            at = @At("RETURN")
    )
    private static void addStaminaAttrs(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(StaminaForTweakers.STAMINA);
        info.getReturnValue().add(StaminaForTweakers.MAX_STAMINA);
    }


    // Shadow methods from Entity

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow
    public abstract double getAttributeBaseValue(EntityAttribute attribute);

    @Unique
    public abstract HungerManager getHungerManager();

    @Shadow
    public abstract @Nullable EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

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
                addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 20, 1, true, false));
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

    @Unique
    private boolean canRecover() {
        return (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileAirborne || isOnGround())
                && (config.recoverUnderwater || !isSubmergedInWater());
    }

    @Unique
    private boolean isNotHungry() {
        return getHungerManager().getFoodLevel() >= 6;
    }

}