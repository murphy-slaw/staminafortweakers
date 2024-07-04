package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.Miner;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.Swimmer;
import net.funkpla.staminafortweakers.registry.Enchantments;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.funkpla.staminafortweakers.util.Timer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
public abstract class ServerPlayerMixin extends PlayerMixin implements Climber, Swimmer, Miner {

    /**
     * A small cooldown to prevent stamina from bouncing around while mining.
     */
    @Unique
    private static final int MINING_COOLDOWN = 10;
    @Unique
    private static final double MIN_RECOVERY = 0.25d;

    @Unique
    private Timer recoveryCooldown = new Timer(config.recoveryExhaustDelayTicks);
    @Unique
    private Vec3 lastPos = new Vec3(0, 0, 0);
    @Unique
    private boolean swimUp;
    @Unique
    private boolean depleted;

    @Shadow
    @Final
    public ServerPlayerGameMode gameMode;

    protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Unique
    private int getTravelingLevel() {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.TRAVELING_ENCHANTMENT, this);
    }

    @Unique
    private boolean hasTraveling() {
        return getTravelingLevel() > 0;
    }

    @Unique
    private float getTravelingModifier() {
        return 1.0F - (getTravelingLevel() / 3.0F);
    }

    @Unique
    private void maybeDamageArmor(EquipmentSlot slot) {
        if (hasTraveling() && this.getRandom().nextFloat() < 0.04f) {
            ItemStack itemStack = this.getItemBySlot(slot);
            itemStack.hurtAndBreak(1, this, player -> player.broadcastBreakEvent(slot));
        }
    }

    @Unique
    private void maybeDamageLeggings() {
        maybeDamageArmor(EquipmentSlot.LEGS);
    }

    @Unique
    private int getUntiringLevel() {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.UNTIRING_ENCHANTMENT, this);
    }

    @Unique
    private float getUntiringModifier() {
        return 1.0F - (getUntiringLevel() / 3.0F);
    }

    @Unique
    private int getEfficiencyLevel() {
        return EnchantmentHelper.getEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.BLOCK_EFFICIENCY, this);
    }

    @Unique
    private float getEfficiencyModifier() {
        if (config.efficiencyExhausts) {
            return 1.0F + (getEfficiencyLevel() * 0.1F);
        }
        return 1.0F;
    }

    @Unique
    private float getMiningModifier() {
        return getEfficiencyModifier() * getUntiringModifier();
    }

    @Unique
    public void setSwamUp(boolean b) {
        swimUp = b;
    }

    @Unique
    public boolean swamUp() {
        return swimUp;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void updateStamina(CallbackInfo ci) {
        if (isCreative() || isSpectator()) return;
        double ySpeed = position().y() - lastPos.y();
        depleted = false;

        if (hasEffect(StatusEffects.TIRELESSNESS)) {
            if (canRecover()) recover();
        } else if (isSwimming() || swamUp()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting() && !isPassenger()) {
            depleteStamina(config.depletionPerTickSprinting * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerJump > 0 && hasJumped()) {
            depleteStamina(config.depletionPerJump * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerTickClimbing > 0 && onClimbable() && ySpeed > 0 && !onGround() && !isSuppressingSlidingDownLadder())
            depleteStamina(config.depletionPerTickClimbing);
        else if (config.depletionPerAttack > 0 && isMining()) {
            depleteStamina(config.depletionPerAttack * getMiningModifier());
            if (recoveryCooldown.expired()) {
                recoveryCooldown = new Timer(MINING_COOLDOWN);
            }
        }
        if (depleted && config.recoveryDelayTicks > 0 && recoveryCooldown.expired()) {
            recoveryCooldown = new Timer(config.recoveryDelayTicks);
        }
        if (canRecover()) recover();
        exhaust();
        lastPos = position();
        recoveryCooldown.tickDown();
        setSwamUp(false);
    }

    @Unique
    private void makeSlow(int amplifier) {
        addEffect(new MobEffectInstance(StatusEffects.FATIGUE, 3, amplifier, true, true));
    }

    @Unique
    private void exhaust() {
        if (isExhausted()) {
            if (config.exhaustionBlackout) {
                addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, true, false));
            }
            if (config.exhaustionSlowsMining) {
                addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 1, true, true));
            }
            makeSlow(4);
            setSprinting(false);
            if (recoveryCooldown.expired()) {
                recoveryCooldown = new Timer(config.recoveryExhaustDelayTicks);
            }
        } else if (isWinded()) makeSlow(2);
        else if (isFatigued()) makeSlow(0);
    }

    @Unique
    private void recover() {
        if (walkDist - walkDistO <= 0.01) {
            recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
        } else if (config.recoverWhileWalking ||
                (config.recoverWhileSneaking && isShiftKeyDown())) {
            recoverStamina(getBaseRecovery());
        }
    }

    @Unique
    private double getBaseRecovery() {
        if (config.formula == StaminaConfig.Formula.LOGARITHMIC) return calcLogRecovery();
        // Formula.LINEAR
        return config.recoveryPerTick;
    }

    @Unique
    private double calcLogRecovery() {
        double r = Math.log(Math.pow((getMaxStamina() - getStamina() + 1), (double) 1 / 3))
                / Math.log(3)
                * config.recoveryPerTick;
        return Math.max(MIN_RECOVERY, r);
    }

    @Unique
    private void recoverStamina(double recoveryAmount) {
        double s = (getMaxStamina() * recoveryAmount / 100);
        setStamina(getStamina() + s);
        if (getStamina() > getMaxStamina()) {
            setStamina(getMaxStamina());
        }
    }

    @Unique
    private void depleteStamina(float depletionAmount) {
        setStamina(getStamina() - depletionAmount);
        if (getStamina() < 0) setStamina(0);
        depleted = true;
    }

    @Unique
    private boolean isStandingStill() {
        return ((walkDist - walkDistO) <= 0.1);
    }

    @Unique
    private boolean canRecover() {
        return !depleted
                && recoveryCooldown.expired()
                && (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileWalking || isStandingStill())
                && (config.recoverWhileAirborne || onGround())
                && (config.recoverUnderwater || !isUnderWater())
                && (config.recoverWhileBreathless || getAirSupply() > 0);
    }

    @Unique
    private boolean isNotHungry() {
        return getFoodData().getFoodLevel() >= 6;
    }

    @Unique
    private boolean isMining() {
        return ((ServerPlayerGameModeMixin) gameMode).getIsDestroyingBlock();
    }

    public void depleteStaminaForBlockBreak() {
        depleteStamina(config.depletionPerBlockBroken * getMiningModifier());
    }
}
