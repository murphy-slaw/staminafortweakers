package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.Swimmer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Climber, Swimmer {

  @Unique public boolean swimUp;

  @ModifyVariable(
      method = "handleOnClimbable(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true)
  protected Vec3 staminafortweakers$replace(Vec3 original) {
    return getClimbSpeed(original);
  }

  @Unique
  public Vec3 getClimbSpeed(Vec3 original) {
    return original;
  }

  @Unique
  public boolean swamUp() {
    return swimUp;
  }

  @Unique
  public void setSwimUp(boolean b) {
    swimUp = b;
  }

  @Inject(method = "jumpInLiquid", at = @At("TAIL"))
  protected void setSwimUpFlag(CallbackInfo ci) {
    setSwimUp(true);
  }

  @Inject(method = "tick", at = @At("HEAD"))
  protected void clearSwimUpFlag(CallbackInfo ci) {
    setSwimUp(false);
  }
}
