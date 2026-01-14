package net.funkpla.staminafortweakers.mixin;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getDepthStrider;

import java.util.Optional;
import net.funkpla.staminafortweakers.*;
import net.funkpla.staminafortweakers.compat.vc_gliders.VCGlidersCompat;
import net.funkpla.staminafortweakers.config.EffectConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.platform.Services;
import net.funkpla.staminafortweakers.platform.services.IPlatformHelper;
import net.funkpla.staminafortweakers.util.Timer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements Swimmer, Miner, Attacker {

  @Unique private static final double MIN_RECOVERY = 0.25d;
  @Unique private final IPlatformHelper platformHelper = Services.PLATFORM;
  @Shadow @Final public ServerPlayerGameMode gameMode;
  @Unique private Timer recoveryCooldown = new Timer(0);
  @Unique private Vec3 lastPos = new Vec3(0, 0, 0);
  @Unique private boolean swimUp;
  @Unique private boolean depleted;
  @Unique private boolean attacked = false;
  @Unique private boolean swungWeapon = false;

  protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Shadow
  public abstract boolean isCreative();

  @Shadow
  public abstract boolean isSpectator();

  @Unique
  private int getTravelingLevel() {
    return EnchantmentHelper.getEnchantmentLevel(Services.REGISTRY.getTravelingEnchantment(), this);
  }

  @Unique
  private boolean hasTraveling() {
    ItemStack leggings = this.getItemBySlot(EquipmentSlot.LEGS);
    if (leggings.getMaxDamage() - leggings.getDamageValue() < config.travelingMinimumDurability)
      return false;
    return getTravelingLevel() > 0;
  }

  @Unique
  @Override
  public float getTravelingModifier() {
    if (!hasTraveling()) return 1F;
    return 1.0F - (getTravelingLevel() / 3.0F);
  }

  @Unique
  @Override
  public float getDepthStriderModifier() {
    return 1.0F - (getDepthStrider(this) / 3.0F);
  }

  @Unique
  private void maybeDamageArmor(EquipmentSlot slot) {
    int roll = this.getRandom().nextInt(100);
    if (hasTraveling() && roll < config.travelingDamageChance) {
      ItemStack itemStack = this.getItemBySlot(slot);
      itemStack.hurtAndBreak(1, this, player -> player.broadcastBreakEvent(slot));
    }
  }

  @Unique
  @Override
  public void maybeDamageLeggings() {
    maybeDamageArmor(EquipmentSlot.LEGS);
  }

  @Unique
  private int getUntiringLevel() {
    return EnchantmentHelper.getEnchantmentLevel(Services.REGISTRY.getUntiringEnchantment(), this);
  }

  @Unique
  private float getUntiringModifier() {
    return 1.0F - (getUntiringLevel() / 3.0F);
  }

  @Unique
  private int getEfficiencyLevel() {
    return EnchantmentHelper.getEnchantmentLevel(
        net.minecraft.world.item.enchantment.Enchantments.BLOCK_EFFICIENCY, this);
  }

  @Unique
  private float getEfficiencyModifier() {
    if (config.efficiencyExhausts) {
      return 1.0F + (getEfficiencyLevel() * 0.1F);
    }
    return 1.0F;
  }

  @Unique
  @Override
  public float getMiningModifier() {
    return getEfficiencyModifier() * getUntiringModifier();
  }

  @Unique
  @Override
  public void setSwamUp(boolean b) {
    swimUp = b;
  }

  @Unique
  @Override
  public boolean swamUp() {
    return swimUp;
  }

  @Unique
  @Override
  public boolean isWading() {
    return isInWater() && hasMovementInput();
  }

  @Inject(method = "tick", at = @At("TAIL"))
  public void updateStamina(CallbackInfo ci) {
    if (isCreative() || isSpectator()) return;
    depleted = false;

    if (shouldExhaust()) Common.getRules().run(((ServerPlayer) (Object) this));

    if (depleted && config.recoveryDelayTicks > 0) {
      recoveryCooldown = new Timer(config.recoveryDelayTicks);
    }
    if (canRecover()) recover();
    exhaust();
    lastPos = position();
    recoveryCooldown.tickDown();
    setSwamUp(false);
    setAttacked(false);
    setSwungWeapon(false);
  }

  @Unique
  @Override
  public boolean shouldClimbExhaust() {
    double ySpeed = position().y() - lastPos.y();
    return onClimbable() && ySpeed > 0 && !onGround();
  }

  @Unique
  @Override
  public boolean shouldSprintExhaust() {
    return isSprinting() && !isPassenger();
  }

  @Unique
  @Override
  public boolean shouldSwimExhaust() {
    return isSwimming() || swamUp() || isWading();
  }

  @Unique
  @Override
  public boolean shouldWadeExhaust() {
    return isWading();
  }

  @Unique
  private void applyEffect(EffectConfig e) {
    var mobEffectInstance = e.getEffectInstance();
    mobEffectInstance.ifPresent(this::addEffect);
  }

  @Unique
  private void exhaust() {
    if (isExhausted()) {
      for (EffectConfig e : config.exhaustedEffects) {
        applyEffect(e);
      }
      setSprinting(false);

      getUsedShield()
          .ifPresent(
              shieldItem -> {
                stopUsingItem();
                ItemCooldowns cooldowns = ((ServerPlayer) (Object) this).getCooldowns();
                if (!cooldowns.isOnCooldown(shieldItem) && config.shieldRecoveryDelayTicks > 0) {
                  cooldowns.addCooldown(shieldItem, config.shieldRecoveryDelayTicks);
                }
              });

      if (platformHelper.isModLoaded("vc_gliders")) VCGlidersCompat.crashGlider(this);

      if (recoveryCooldown.expired()) {
        recoveryCooldown = new Timer(config.recoveryExhaustDelayTicks);
      }
    } else if (isWinded()) {
      for (EffectConfig e : config.windedEffects) {
        applyEffect(e);
      }
    } else if (isFatigued()) {
      for (EffectConfig e : config.fatiguedEffects) {
        applyEffect(e);
      }
    }
  }

  @Unique
  private void recover() {
    if (isStandingStill()) recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
    else recoverStamina(getBaseRecovery());
  }

  @Unique
  private double getBaseRecovery() {
    if (config.formula == StaminaConfig.Formula.LOGARITHMIC) return calcLogRecovery();
    // Formula.LINEAR
    return config.recoveryPerTick;
  }

  @Unique
  private double calcLogRecovery() {
    double r =
        Math.log(Math.pow((getMaxStamina() - getStamina() + 1), (double) 1 / 3))
            / Math.log(3)
            * config.recoveryPerTick;
    return Math.max(MIN_RECOVERY, r);
  }

  @Unique
  private void recoverStamina(double recoveryAmount) {
    double s = (getMaxStamina() * recoveryAmount / 100);
    int compared = Double.compare(getStamina(), getMaxStamina());
    if (compared > 0) setStamina(getMaxStamina());
    else if (compared < 0) setStamina(getStamina() + s);
  }

  @Unique
  @Override
  public void depleteStamina(float depletionAmount) {
    setStamina(getStamina() - depletionAmount);
    if (getStamina() < 0) setStamina(0);
    depleted = true;
  }

  @Unique
  private boolean isStandingStill() {
    return !hasMovementInput();
  }

  @Unique
  private boolean canRecover() {
    return !depleted
        && recoveryCooldown.expired()
        && (config.recoverWhenHungry || isNotHungry())
        && ((config.recoverWhileSneaking && isSteppingCarefully())
            || (config.recoverWhileWalking || isStandingStill()))
        && (config.recoverWhileAirborne || isOnGroundOrMounted())
        && (config.recoverUnderwater || !isUnderWater())
        && (config.recoverWhileBreathless || getAirSupply() > 0)
        && !isUsingShield();
  }

  @Unique
  public boolean isOnGroundOrMounted() {
    return onGround() || getVehicle() != null;
  }

  @Unique
  private boolean isNotHungry() {
    return getFoodData().getFoodLevel() >= 6;
  }

  @Unique
  @Override
  public boolean isMining() {
    return Services.PLATFORM.isDestroyingBlock(gameMode);
  }

  @Unique
  public void depleteStaminaForBlockBreak() {
    depleteStamina(config.depletionPerBlockBroken * getMiningModifier());
  }

  @Unique
  @Override
  public boolean isUsingShield() {
    return this.isUsingItem() && this.getItemInHand(this.getUsedItemHand()).is(Constants.SHIELDS);
  }

  @Unique
  public Optional<Item> getUsedShield() {
    if (isUsingItem()) {
      ItemStack stack = this.getItemInHand(this.getUsedItemHand());
      if (stack.is(Constants.SHIELDS)) return Optional.of(stack.getItem());
    }
    return Optional.empty();
  }

  @Unique
  @Override
  public boolean getAttacked() {
    return attacked;
  }

  @Unique
  @Override
  public void setAttacked(boolean attacked) {
    this.attacked = attacked;
  }

  @Unique
  @Override
  public boolean getSwungWeapon() {
    return this.swungWeapon;
  }

  @Unique
  @Override
  public void setSwungWeapon(boolean swung) {
    this.swungWeapon = swung;
  }
}
