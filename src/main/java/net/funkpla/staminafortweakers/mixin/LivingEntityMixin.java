package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Climber {

    @Shadow
    protected abstract float modifyAppliedDamage(DamageSource source, float amount);

    @Shadow
    public abstract boolean canBreatheInWater();

    @Shadow
    public abstract void clearActiveItem();

    @Shadow
    protected abstract void onStatusEffectRemoved(StatusEffectInstance effect);

    @Inject(
            method = "Lnet/minecraft/entity/LivingEntity;createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1, allow = 1, at = @At("RETURN")
    )
    private static void staminafortweakers$addPlayerAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(StaminaForTweakers.CLIMB_SPEED);
    }

    @ModifyVariable(
            method = "Lnet/minecraft/entity/LivingEntity;applyClimbingSpeed(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    protected Vec3d staminafortweakers$replace(Vec3d original) {
        return getClimbSpeed(original);
    }

    public Vec3d getClimbSpeed(Vec3d original) {
        return original;
    }
}
