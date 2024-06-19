package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements Climber {
    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

    private Vec3d lastPos = new Vec3d(0, 0, 0);

    @Inject(method = "tick", at = @At("HEAD"))
    public void updateStamina(CallbackInfo ci) {
        double ySpeed = getPos().getY() - lastPos.getY();
        if (isSwimming()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting()) depleteStamina(config.depletionPerTickSprinting);
        else if (config.depletionPerJump > 0 && hasJumped()) depleteStamina(config.depletionPerJump);
        else if (config.depletionPerTickClimbing > 0 && isClimbing() && ySpeed > 0 && !isOnGround() && !isHoldingOntoLadder())
            depleteStamina(config.depletionPerTickClimbing);
        else if (canRecover()) doRecovery();
        doExhaustion();
        lastPos = getPos();
    }

    @Unique
    private void makeSlow(int amplifier) {
        addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3, amplifier, true, false));
        updateClimbModifiers();
    }

    private int breathCount = 0;
    private static final int BREATH_TICKS = 40;

    @Unique
    private void doExhaustion() {
        double pct = getExhaustionPct();
        if (pct <= config.exhaustedPercentage) {
            if (config.exhaustionBlackout) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 20, 1, true, false));
            }
            makeSlow(4);
            setSprinting(false);
        } else if (pct <= config.windedPercentage) makeSlow(2);
        else if (pct <= config.fatiguedPercentage) makeSlow(1);

        if (!config.exhaustionSounds) return;

        breathCount += (int) (Math.random() * 3);
        if (breathCount >= BREATH_TICKS) breathCount = 0;
        if (pct <= config.windedPercentage) {
            if (breathCount == 0) {
                playSound(StaminaForTweakers.ENTITY_PLAYER_PANT, SoundCategory.PLAYERS, 0.8f, (float) (Math.random() * 0.25f) + 0.875f);
            }
        }
    }

    @Unique
    private void doRecovery() {
        if (horizontalSpeed - prevHorizontalSpeed <= 0.01) {
            recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
        } else if (config.recoverWhileWalking ||
                (config.recoverWhileSneaking && isSneaking())) {
            recoverStamina(getBaseRecovery());
        }
    }

    private double getBaseRecovery() {
        if (config.formula == StaminaConfig.Formula.LOGARITHMIC) return calcLogRecovery();
        // Formula.LINEAR
        return config.recoveryPerTick;
    }

    private double calcLogRecovery() {
        return Math.log(Math.pow((getMaxStamina() - getStamina() + 1), (double) 1 / 3))
                / Math.log(3)
                * config.recoveryPerTick;
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
}
