package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Climber;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerAttributeMixin extends LivingEntity implements Climber, Exhaustible {

  protected PlayerAttributeMixin(EntityType<? extends LivingEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Inject(
      method =
          "createAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
      require = 1,
      allow = 1,
      at = @At("RETURN"))
  private static void staminafortweakers$addPlayerAttributes(
      final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
    info.getReturnValue().add(Services.REGISTRY.getStaminaAttribute());
    info.getReturnValue().add(Services.REGISTRY.getMaxStaminaAttribute());
    info.getReturnValue().add(Services.REGISTRY.getStaminaRecoveryAttribute());
  }
}
