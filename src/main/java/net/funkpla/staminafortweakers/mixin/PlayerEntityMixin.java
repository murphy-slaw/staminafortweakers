package net.funkpla.staminafortweakers.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Climber {

    @Inject(method = "createPlayerAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", require = 1, allow = 1, at = @At("RETURN"))
    private static void staminafortweakers$addPlayerAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(StaminaForTweakers.STAMINA);
        info.getReturnValue().add(StaminaForTweakers.MAX_STAMINA);
    }

    @Unique
    protected final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    @Unique
    private boolean jumped;

    protected boolean movedUp;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract HungerManager getHungerManager();

    @Shadow
    public abstract PlayerAbilities getAbilities();

    @Shadow
    public abstract void jump();

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    protected boolean isSubmergedInWater;

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


    protected boolean hasJumped() {
        return jumped;
    }

    public void updateClimbModifiers() {
        EntityAttributeInstance climbSpeed = getAttributeInstance(StaminaForTweakers.CLIMB_SPEED);
        Set<EntityAttributeModifier> curMods = climbSpeed.getModifiers();
        EntityAttributeInstance moveSpeed = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        Set<EntityAttributeModifier> targetMods = moveSpeed.getModifiers();
        if (!curMods.equals(targetMods)) {
            StaminaForTweakers.LOGGER.info("targetMods: {}", targetMods);
            climbSpeed.clearModifiers();
            for (EntityAttributeModifier modifier : moveSpeed.getModifiers()) {
                climbSpeed.addPersistentModifier(modifier);
            }
        }
    }

    public Vec3d getClimbSpeed(Vec3d original) {
        EntityAttributeInstance climbSpeed = getAttributeInstance(StaminaForTweakers.CLIMB_SPEED);
        if (climbSpeed == null || original.y <= 0 || (jumping && !isClimbing())) return original;
        updateClimbModifiers();
        climbSpeed.setBaseValue(original.y);
        return new Vec3d(original.x, climbSpeed.getValue(), original.z);
    }

}
