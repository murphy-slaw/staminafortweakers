package net.funkpla.staminafortweakers.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    @Inject(method = "createPlayerAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", require = 1, allow = 1, at = @At("RETURN"))
    private static void staminafortweakers$addPlayerAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(StaminaForTweakers.STAMINA);
        info.getReturnValue().add(StaminaForTweakers.MAX_STAMINA);
    }

    @Unique
    protected final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    @Unique
    private boolean jumped;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract HungerManager getHungerManager();


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

}
