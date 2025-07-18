package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Attacker;
import net.funkpla.staminafortweakers.Miner;
import net.funkpla.staminafortweakers.Swimmer;
import net.funkpla.staminafortweakers.compat.Mods;
import net.funkpla.staminafortweakers.compat.vc_gliders.VCGlidersCompat;
import net.funkpla.staminafortweakers.config.EffectConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.packet.S2CSenders;
import net.funkpla.staminafortweakers.util.Timer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
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
public abstract class ServerPlayerMixin extends PlayerMixin implements Swimmer, Miner, Attacker {

    /**
     * A small cooldown to prevent stamina from bouncing around while mining.
     */
    @Unique
    private static final int MINING_COOLDOWN = 10;
    @Unique
    private static final double MIN_RECOVERY = 0.25d;
    @Shadow
    @Final
    public ServerPlayerGameMode gameMode;
    @Unique
    private Timer recoveryCooldown = new Timer(config.recoveryExhaustDelayTicks);
    @Unique
    private Timer shieldCooldown = new Timer(config.shieldRecoveryDelayTicks);
    @Unique
    private Vec3 lastPos = new Vec3(0, 0, 0);
    @Unique
    private boolean swimUp;
    @Unique
    private boolean depleted;
    @Unique
    private boolean attacked = false;

    protected ServerPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @Unique
    private int getEnchantmentLevel(String name) {
        ResourceKey<Enchantment> providerKey = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(name));
        RegistryAccess registryManager = level().registryAccess();
        Optional<Holder.Reference<Enchantment>> enchantment = registryManager.registryOrThrow(Registries.ENCHANTMENT).getHolder(providerKey);
        return enchantment.map(enchantmentReference -> EnchantmentHelper.getEnchantmentLevel(enchantmentReference, this)).orElse(0);
    }

    @Unique
    private int getTravelingLevel() {
        return getEnchantmentLevel("staminafortweakers:traveling");
    }

    @Unique
    private int getDepthStriderLevel() {
        return getEnchantmentLevel("minecraft:depth_strider");
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
    private float getDepthStriderModifier(){
        return 1.0F - (getDepthStriderLevel() / 3.0F);
    }

    @Unique
    private void maybeDamageArmor(EquipmentSlot slot) {
        if (hasTraveling() && this.getRandom().nextFloat() < 0.04f) {
            ItemStack itemStack = this.getItemBySlot(slot);
            itemStack.hurtAndBreak(1, this, slot);
        }
    }

    @Unique
    private void maybeDamageLeggings() {
        maybeDamageArmor(EquipmentSlot.LEGS);
    }

    @Unique
    private int getUntiringLevel() {
        return getEnchantmentLevel("staminafortweakers:untiring");
    }

    @Unique
    private float getUntiringModifier() {
        return 1.0F - (getUntiringLevel() / 3.0F);
    }

    @Unique
    private int getEfficiencyLevel() {
        return getEnchantmentLevel("minecraft:efficiency");
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

    @Override
    public void setSwamUp(boolean b) {
        swimUp = b;
    }

    @Override
    public boolean swamUp() {
        return swimUp;
    }

    @Unique
    public boolean isWading() {
        return isInWater() && hasMovementInput();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void updateStamina(CallbackInfo ci) {
        if (isCreative() || isSpectator())
            return;
        double ySpeed = position().y() - lastPos.y();
        depleted = false;

        if (shouldExhaust()) {
            if (isSwimming() || swamUp() || isWading()) {
                depleteStamina(config.depletionPerTickSwimming * getDepthStriderModifier());
            } else if (isSprinting() && !isPassenger()) {
                depleteStamina(config.depletionPerTickSprinting * getTravelingModifier());
                maybeDamageLeggings();
            }
            if (config.depletionPerJump > 0 && hasJumped()) {
                depleteStamina(config.depletionPerJump * getTravelingModifier());
                maybeDamageLeggings();
            }
            if (config.depletionPerTickClimbing > 0 && onClimbable() && ySpeed > 0 && !onGround())
                depleteStamina(config.depletionPerTickClimbing);
            if (config.depletionPerMiningTick > 0 && isMining()) {
                depleteStamina(config.depletionPerMiningTick * getMiningModifier());
                if (recoveryCooldown.expired()) {
                    recoveryCooldown = new Timer(MINING_COOLDOWN);
                }
            }
            if (config.depletionPerAttack > 0 && getAttacked()) {
                depleteStamina(config.depletionPerAttack);
            }
            if (config.depletionPerShieldTick > 0 && isUsingShield()) {
                depleteStamina(config.depletionPerShieldTick);
            }
            if (Mods.VC_GLIDERS.isLoaded()) {
                if (config.depletionPerGliderTick > 0 && VCGlidersCompat.isGliding(this)) {
                    depleteStamina(config.depletionPerGliderTick);
                }
            }
        }
        if (depleted && config.recoveryDelayTicks > 0 && recoveryCooldown.expired()) {
            recoveryCooldown = new Timer(config.recoveryDelayTicks);
        }
        if (canRecover())
            recover();
        exhaust();
        lastPos = position();
        recoveryCooldown.tickDown();
        shieldCooldown.tickDown();
        if (shieldCooldown.expired())
            setShieldAllowed(true);
        setSwamUp(false);
        //setHasMovementInput(false);
        setAttacked(false);
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
            setShieldAllowed(false);
            if (isUsingShield())
                stopUsingItem();
            if (shieldCooldown.expired())
                shieldCooldown = new Timer(config.recoveryExhaustDelayTicks);
            if (Mods.VC_GLIDERS.isLoaded())
                VCGlidersCompat.crashGlider(this);
            if (recoveryCooldown.expired()) {
                recoveryCooldown = new Timer(config.recoveryExhaustDelayTicks);
            }
        } else if (isWinded()) {
            for (EffectConfig e : config.windedEffects) {
                applyEffect(e);
            }
        } else if (isFatigued()) {
            for (EffectConfig e : config.windedEffects) {
                applyEffect(e);
            }
        }
    }

    @Unique
    private void recover() {
        if (isStandingStill())
            recoverStamina(getBaseRecovery() * config.recoveryRestBonusMultiplier);
        else
            recoverStamina(getBaseRecovery());
    }

    @Unique
    private double getBaseRecovery() {
        if (config.formula == StaminaConfig.Formula.LOGARITHMIC)
            return calcLogRecovery();
        // Formula.LINEAR
        return config.recoveryPerTick;
    }

    @Unique
    private double calcLogRecovery() {
        double r =
                Math.log(Math.pow((getMaxStamina() - getStamina() + 1), (double) 1 / 3)) / Math.log(3) * config.recoveryPerTick;
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
        if (getStamina() < 0)
            setStamina(0);
        depleted = true;
    }

    @Unique
    private boolean isStandingStill() {
        return !hasMovementInput();
    }

    @Unique
    private boolean canRecover() {
        return !depleted && recoveryCooldown.expired() && (config.recoverWhenHungry || isNotHungry()) && ((config.recoverWhileSneaking && isShiftKeyDown()) || (config.recoverWhileWalking || isStandingStill())) && (config.recoverWhileAirborne || onGround()) && (config.recoverUnderwater || !isUnderWater()) && (config.recoverWhileBreathless || getAirSupply() > 0) && !isUsingShield();
    }

    @Unique
    private boolean isNotHungry() {
        return getFoodData().getFoodLevel() >= 6;
    }

    @Unique
    private boolean isMining() {
        return ((ServerPlayerGameModeMixin) gameMode).getIsDestroyingBlock();
    }

    @Override
    @Unique
    public void depleteStaminaForBlockBreak() {
        depleteStamina(config.depletionPerBlockBroken * getMiningModifier());
    }

    @Unique
    public boolean isUsingShield() {
        return this.isUsingItem() && this.getItemInHand(this.getUsedItemHand()).getItem() == Items.SHIELD;
    }

    @Unique
    @Override
    public void setShieldAllowed(boolean allowed) {
        if (isShieldAllowed() == allowed)
            return;
        super.setShieldAllowed(allowed);
        S2CSenders.sendShieldAllowedPacket((ServerPlayer) (Object) this, allowed);
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
}
