package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.combat_roll.internals.RollManager", remap = false)
public abstract class RollManagerMixin {

  @Shadow private int currentCooldownLength;

  @Inject(method = "isRollAvailable", at = @At("HEAD"), cancellable = true)
  private void blockRollWhenFatigued(Player player, CallbackInfoReturnable<Boolean> cir) {
    if (!Common.getConfig().combatRollCompat) return;
    if (player.isCreative() || player.isSpectator()) return;
    if (((Exhaustible) player).isFatigued()) {
      cir.setReturnValue(false);
    }
  }

  @Inject(method = "updateCooldownLength", at = @At("TAIL"))
  private void overrideCooldownLength(LocalPlayer player, CallbackInfo ci) {
    StaminaConfig config = Common.getConfig();
    if (config.combatRollCompat && config.combatRollCooldownTicks >= 0) {
      currentCooldownLength = config.combatRollCooldownTicks;
    }
  }
}
