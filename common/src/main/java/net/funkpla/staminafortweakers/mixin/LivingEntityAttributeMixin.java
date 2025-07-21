package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.Swimmer;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin implements Climber, Swimmer {

  @Inject(
      method =
          "createLivingAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
      require = 1,
      allow = 1,
      at = @At("RETURN"))
  private static void staminafortweakers$addPlayerAttributes(
      final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
      info.getReturnValue().add(Services.REGISTRY.getClimbSpeedAttribute());
  }
}
