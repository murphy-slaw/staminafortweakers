package net.funkpla.staminafortweakers.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.funkpla.staminafortweakers.mixin.PlayerEntityMixin;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntityMixin {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();

    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void playSound(SoundEvent event, float volume, float pitch);

    private int breathCount = 0;
    private static final int BREATH_TICKS = 40;

    @Inject(method = "tick", at = @At("HEAD"))
    private void doExhaustionSounds(CallbackInfo ci) {
        if (isSubmergedInWater()) return;
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
            method = "canSprint()Z",
            at = @At("HEAD"),
            cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        double stamina = this.getAttributeValue(StaminaForTweakers.STAMINA);
        double max_stamina = this.getAttributeValue(StaminaForTweakers.MAX_STAMINA);
        boolean isNotExhausted = ((stamina / max_stamina) * 100) <= config.exhaustedPercentage;
        cir.setReturnValue(
                isNotExhausted || this.hasVehicle() || (float) this.getHungerManager().getFoodLevel() > 6.0F || this.getAbilities().allowFlying
        );
    }

}