package net.funkpla.staminafortweakers.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.Swimmer;
import net.funkpla.staminafortweakers.mixin.PlayerMixin;
import net.funkpla.staminafortweakers.packet.client.C2SSenders;
import net.funkpla.staminafortweakers.registry.SoundEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends PlayerMixin implements Swimmer {
    @Unique
    private static final int BREATH_TICKS = 20;
    @Unique
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    @Unique
    private int breathCount = 0;

    protected LocalPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void playSound(SoundEvent event, float volume, float pitch);

    @Shadow
    public abstract boolean isUnderWater();

    @Inject(method = "tick", at = @At("HEAD"))
    private void doExhaustionSounds(CallbackInfo ci) {
        if (isUnderWater()) return;
        if (!config.exhaustionSounds) return;
        breathCount += this.random.nextInt(2);
        if (breathCount >= BREATH_TICKS) {
            breathCount = 0;
            if (isWinded()) this.playSound(SoundEvents.ENTITY_PLAYER_PANT, 0.8f, (float) (Math.random() * 0.25f) + 0.875f);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void sendSwimPacket(CallbackInfo ci) {
        if (swamUp()) {
            C2SSenders.sendSwimPacket();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void sendMovementInputPacket(CallbackInfo ci) {
        if (zza != 0 || xxa != 0){
            C2SSenders.sendMovementInputPacket();
        }
    }

    @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        if(isExhausted()){
            cir.setReturnValue(false);
        }
    }
}