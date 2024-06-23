package net.funkpla.staminafortweakers.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.funkpla.staminafortweakers.mixin.PlayerMixin;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends PlayerMixin {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    protected LocalPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void playSound(SoundEvent event, float volume, float pitch);

    @Shadow
    public abstract boolean isUnderWater();

    private int breathCount = 0;
    private static final int BREATH_TICKS = 40;

    @Inject(method = "tick", at = @At("HEAD"))
    private void doExhaustionSounds(CallbackInfo ci) {
        if (isUnderWater()) return;
        double pct = getExhaustionPct();
        if (!config.exhaustionSounds) return;

        breathCount += (int) (Math.random() * 3);
        if (breathCount >= BREATH_TICKS) breathCount = 0;
        if (pct <= config.windedPercentage) {
            if (breathCount == 0) {
                this.playSound(StaminaForTweakers.ENTITY_PLAYER_PANT, 0.8f, (float) (Math.random() * 0.25f) + 0.875f);
            }
        }

    }

    @Inject(
            method = "canStartSprinting()Z",
            at = @At("HEAD"),
            cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        double stamina = this.getAttributeValue(StaminaForTweakers.STAMINA);
        double max_stamina = this.getAttributeValue(StaminaForTweakers.MAX_STAMINA);
        boolean isNotExhausted = ((stamina / max_stamina) * 100) <= config.exhaustedPercentage;
        cir.setReturnValue(
                isNotExhausted || this.isPassenger() || (float) this.getFoodData().getFoodLevel() > 6.0F || this.getAbilities().mayfly
        );
    }

}