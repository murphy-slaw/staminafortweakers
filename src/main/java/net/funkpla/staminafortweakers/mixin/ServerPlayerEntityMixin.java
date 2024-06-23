package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.RecoveryDelayTimer;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements Climber {

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;

    @Shadow
    public abstract void travel(Vec3d movementInput);

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private RecoveryDelayTimer timer = new RecoveryDelayTimer(config.recoveryDelayTicks);

    private Vec3d lastPos = new Vec3d(0, 0, 0);

    private int getTravelingLevel() {
        RegistryKey<Enchantment> providerKey = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of("staminafortweakers:traveling"));
        DynamicRegistryManager registryManager = getWorld().getRegistryManager();
        Optional<RegistryEntry.Reference<Enchantment>> traveling = registryManager.get(RegistryKeys.ENCHANTMENT).getEntry(providerKey);
        return traveling.map(enchantmentReference -> EnchantmentHelper.getEquipmentLevel(enchantmentReference, this)).orElse(0);
    }

    private boolean hasTraveling() {
        return getTravelingLevel() > 0;
    }

    private float getTravelingModifier() {
        return 1.0F - (getTravelingLevel() / 3.0F);
    }

    private void maybeDamageArmor(EquipmentSlot slot) {
        if (hasTraveling() && this.getRandom().nextFloat() < 0.04f) {
            ItemStack itemStack = this.getEquippedStack(slot);
            Item item = itemStack.getItem();
            itemStack.damage(1, this, slot);
        }
    }

    private void maybeDamageLeggings() {
        maybeDamageArmor(EquipmentSlot.LEGS);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void updateStamina(CallbackInfo ci) {
        if (isCreative() || isSpectator()) return;
        double ySpeed = getPos().getY() - lastPos.getY();

        if (hasStatusEffect(StaminaForTweakers.TIRELESSNESS)) {
            if (canRecover()) doRecovery();
        } else if (isSwimming()) depleteStamina(config.depletionPerTickSwimming);
        else if (isSprinting()) {
            depleteStamina(config.depletionPerTickSprinting * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerJump > 0 && hasJumped()) {
            depleteStamina(config.depletionPerJump * getTravelingModifier());
            maybeDamageLeggings();
        } else if (config.depletionPerTickClimbing > 0 && isClimbing() && ySpeed > 0 && !isOnGround() && !isHoldingOntoLadder())
            depleteStamina(config.depletionPerTickClimbing);
        else if (config.depletionPerAttack > 0 && isMining()) {
            depleteStamina(config.depletionPerAttack);
        } else if (canRecover()) doRecovery();
        doExhaustion();
        lastPos = getPos();
        timer.tickDown();
    }

    @Unique
    private void makeSlow(int amplifier) {
        addStatusEffect(new StatusEffectInstance(StaminaForTweakers.FATIGUE, 3, amplifier, true, true));
    }


    @Unique
    private void doExhaustion() {
        double pct = getExhaustionPct();
        if (pct <= config.exhaustedPercentage) {
            if (config.exhaustionBlackout) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 1, true, false));
            }
            if (config.exhaustionSlowsMining) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 20, 1, true, true));
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
        return timer.expired() &&
                (config.recoverWhenHungry || isNotHungry())
                && (config.recoverWhileWalking || isStandingStill())
                && (config.recoverWhileAirborne || isOnGround())
                && (config.recoverUnderwater || !isSubmergedInWater())
                && (config.recoverWhileBreathless || getAir() > 0);
    }

    @Unique
    private boolean isNotHungry() {
        return getHungerManager().getFoodLevel() >= 6;
    }

    private boolean isMining() {
        return ((ServerPlayerInteractionManagerMixin) interactionManager).getMining();
    }
}
