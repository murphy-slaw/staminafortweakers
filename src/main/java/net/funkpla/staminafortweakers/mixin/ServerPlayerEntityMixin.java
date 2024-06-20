package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements Climber {
    @Shadow
    public abstract ServerWorld getServerWorld();

    @Shadow
    protected abstract GameMode getServerGameMode(@Nullable GameMode backupGameMode);

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpawnForced();

    @Shadow
    public abstract boolean isSpectator();

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    private Vec3d lastPos = new Vec3d(0, 0, 0);

    @Inject(method = "tick", at = @At("HEAD"))
    public void updateStamina(CallbackInfo ci) {
        if (isCreative() || isSpectator()) return;
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


    @Unique
    private void doExhaustion() {
        double pct = getExhaustionPct();
        if (pct <= config.exhaustedPercentage) {
            if (config.exhaustionBlackout) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 1, true, false));
            }
            makeSlow(4);
            setSprinting(false);
        } else if (pct <= config.windedPercentage) makeSlow(2);
        else if (pct <= config.fatiguedPercentage) makeSlow(1);

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
