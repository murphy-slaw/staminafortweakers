package net.funkpla.staminafortweakers.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.Jumper;
import net.funkpla.staminafortweakers.StaminaConfig;
import net.funkpla.staminafortweakers.StaminaForTweakers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Jumper {
    @Unique
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    @Unique
    private boolean jumped;

    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1,
            allow = 1,
            at = @At("RETURN")
    )
    private static void addStamina(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(StaminaForTweakers.STAMINA);
        info.getReturnValue().add(StaminaForTweakers.MAX_STAMINA);
    }

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Inject(method = "tick", at = @At("HEAD"))
    private void clearJumpedFlag(CallbackInfo ci) {
        jumped = false;
    }

    @Inject(method = "jump", at = @At("TAIL"))
    private void setJumpedFlag(CallbackInfo ci) {
        jumped = true;
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void blockJumping(CallbackInfo ci) {
        if (!config.canJumpWhileExhausted) {
            double stamina = this.getAttributeValue(StaminaForTweakers.STAMINA);
            double maxStamina = this.getAttributeValue(StaminaForTweakers.MAX_STAMINA);
            double exhaustionPercentage = (stamina / maxStamina) * 100;
            if (exhaustionPercentage <= config.exhaustedPercentage) ci.cancel();
        }
    }

    @Override
    public boolean hasJumped() {
        return jumped;
    }
}