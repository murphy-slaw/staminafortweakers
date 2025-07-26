package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Swimmer;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ILivingEntityExtension.class)
public interface LivingEntityFluidMixin extends Swimmer{
    @Inject(method = "jumpInFluid", at = @At("TAIL"))
    default void setSwimUpFlag(CallbackInfo ci) {
        this.setSwamUp(true);
    }
}
