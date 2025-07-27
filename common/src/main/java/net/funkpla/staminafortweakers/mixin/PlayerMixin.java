package net.funkpla.staminafortweakers.mixin;

import java.util.Optional;
import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.SimpleEffectConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Climber, Exhaustible {

  @Unique
  protected final StaminaConfig config =
      AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

  @Unique protected boolean shieldAllowed = true;
  @Unique protected boolean hasMovementInput = false;
  @Unique private boolean jumped;

  protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Inject(
      method =
          "createAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
      require = 1,
      allow = 1,
      at = @At("RETURN"))
  private static void staminafortweakers$addPlayerAttributes(
      final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
    if (Services.PLATFORM.getPlatformName().equals("Fabric")) {
      info.getReturnValue().add(Services.REGISTRY.getStaminaAttribute());
      info.getReturnValue().add(Services.REGISTRY.getMaxStaminaAttribute());
    }
  }

  @Shadow
  public abstract void jumpFromGround();

  @Shadow
  public abstract float getSpeed();

  @Shadow
  public abstract FoodData getFoodData();

  @Shadow
  protected abstract int getFireImmuneTicks();

  @Unique
  public int getExhaustionPct() {
    return (int) (getStamina() / getMaxStamina() * 100);
  }

  @Unique
  @Override
  public boolean isExhausted() {
    return getExhaustionPct() <= config.exhaustedPercentage;
  }

  @Unique
  public boolean isWinded() {
    return getExhaustionPct() <= config.windedPercentage;
  }

  @Unique
  public boolean isFatigued() {
    return getExhaustionPct() <= config.fatiguedPercentage;
  }

  @Unique
  public double getMaxStamina() {
    return getAttributeBaseValue(Services.REGISTRY.getMaxStaminaAttribute());
  }

  @Unique
  public double getStamina() {
    return getAttributeBaseValue(Services.REGISTRY.getStaminaAttribute());
  }

  @Unique
  public void setStamina(double stamina) {
    getAttribute(Services.REGISTRY.getStaminaAttribute()).setBaseValue(stamina);
  }

  /**
   * Track whether the entity jumped this tick. Set the flag in jump() and clear it at the beginning
   * of every tick.
   */
  @Inject(method = "tick", at = @At("HEAD"))
  private void clearFlags(CallbackInfo ci) {
    jumped = false;
  }

  @Inject(method = "jumpFromGround", at = @At("TAIL"))
  private void setJumpedFlag(CallbackInfo ci) {
    jumped = true;
  }

  /** Inject into LivingEntity.jump() to block jumping according to config. */
  @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
  private void blockJumping(CallbackInfo ci) {
    if (config.canJumpWhileExhausted) return;
    if (getExhaustionPct() <= config.exhaustedPercentage) ci.cancel();
  }

  @Unique
  protected boolean hasJumped() {
    return jumped;
  }

  @Override
  public Vec3 getClimbSpeed(Vec3 original) {
    AttributeInstance climbSpeed = getAttribute(Services.REGISTRY.getClimbSpeedAttribute());
    if (climbSpeed == null || original.y <= 0 || (jumping && !onClimbable())) return original;
    climbSpeed.setBaseValue(original.y);
    return new Vec3(original.x, climbSpeed.getValue(), original.z);
  }

  @Override
  public boolean shouldExhaust() {
    for (SimpleEffectConfig s : config.untiringEquivalentEffects) {
      Optional<MobEffect> m = s.getEffect();
      if (m.isPresent() && hasEffect(m.get())) return false;
    }
    return !hasEffect(Services.REGISTRY.getTirelessnessEffect());
  }

  @Unique
  public boolean hasMovementInput() {
    return hasMovementInput;
  }

  @Unique
  public void setHasMovementInput(boolean b) {
    hasMovementInput = b;
  }

  @Unique
  @Override
  public boolean isShieldAllowed() {
    return shieldAllowed;
  }
}
