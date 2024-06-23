package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.RecoveryDelayTimer;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
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

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements Climber {

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    @Final
    public net.minecraft.server.level.ServerPlayerGameMode gameMode;

    @Shadow
    public abstract void travel(Vec3 movementInput);

    protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    private RecoveryDelayTimer timer = new RecoveryDelayTimer(config.recoveryDelayTicks);

    private Vec3 lastPos = new Vec3(0, 0, 0);

    private int getTravelingLevel() {
        ResourceKey<Enchantment> providerKey = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("staminafortweakers:traveling"));
        RegistryAccess registryManager = level().registryAccess();
        Optional<Holder.Reference<Enchantment>> traveling = registryManager.registryOrThrow(Registries.ENCHANTMENT).getHolder(providerKey);
        return traveling.map(enchantmentReference -> EnchantmentHelper.getEnchantmentLevel(enchantmentReference, this)).orElse(0);
    }

    private boolean hasTraveling() {
        return getTravelingLevel() > 0;
    }

    private float getTravelingModifier() {
        return 1.0F - (getTravelingLevel() / 3.0F);
    }

    private void maybeDamageArmor(EquipmentSlot slot) {
        if (hasTraveling() && this.getRandom().nextFloat() < 0.04f) {
            ItemStack itemStack = this.getItemBySlot(slot);
            Item item = itemStack.getItem();
            itemStack.hurtAndBreak(1, this, slot);
        }
    }

    private void maybeDamageLeggings() {
        maybeDamageArmor(EquipmentSlot.LEGS);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void updateStamina(CallbackInfo ci) {
        if (isCreative() || isSpectator()) return;
        double ySpeed = position().y() - lastPos.y();

        if (hasEffect(StaminaForTweakers.TIRELESSNESS)) {
            if (canRecover()) doRecovery();
        } else if (isSwimming()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting()) {
            depleteStamina(config.depletionPerTickSprinting * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerJump > 0 && hasJumped()) {
            depleteStamina(config.depletionPerJump * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerTickClimbing > 0 && onClimbable() && ySpeed > 0 && !onGround() && !isSuppressingSlidingDownLadder())
            depleteStamina(config.depletionPerTickClimbing);
        else if (config.depletionPerAttack > 0 && isMining()) {
            depleteStamina(config.depletionPerAttack);
        } else if (canRecover()) doRecovery();
        doExhaustion();
        lastPos = position();
        timer.tickDown();
    }

    @Unique
    private void makeSlow(int amplifier) {
        addEffect(new MobEffectInstance(StaminaForTweakers.FATIGUE, 3, amplifier, true, true));
    }


    @Unique
    private void doExhaustion() {
        double pct = getExhaustionPct();
        if (pct <= config.exhaustedPercentage) {
            if (config.exhaustionBlackout) {
                addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 1, true, false));
            }
            if (config.exhaustionSlowsMining) {
                addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 1, true, true));
            }
            makeSlow(4);
            setSprinting(false);
            if (timer.expired()) {
                timer = new RecoveryDelayTimer(config.recoveryDelayTicks);
            }
        } else if (pct <= config.windedPercentage) makeSlow(2);
        else if (pct <= config.fatiguedPercentage) makeSlow(1);

    }

    @Unique
    private void doRecovery() {
        if (walkDist - walkDistO <= 0.01) {
            recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
        } else if (config.recoverWhileWalking ||
                (config.recoverWhileSneaking && isShiftKeyDown())) {
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
        return ((walkDist - walkDistO) <= 0.1);
    }

    @Unique
    private boolean canRecover() {
        return timer.expired() &&
                (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileWalking || isStandingStill())
                && (config.recoverWhileAirborne || onGround())
                && (config.recoverUnderwater || !isUnderWater())
                && (config.recoverWhileBreathless || getAirSupply() > 0);
    }

    @Unique
    private boolean isNotHungry() {
        return getFoodData().getFoodLevel() >= 6;
    }

    private boolean isMining() {
        return ((ServerPlayerGameMode) gameMode).getIsDestroyingBlock();
    }
}
