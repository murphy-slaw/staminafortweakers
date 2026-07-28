package net.funkpla.staminafortweakers.mixin;

import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.combatroll.client.gui.HudRenderHelper", remap = false)
public abstract class HudRenderHelperMixin {

  @Inject(method = "render", at = @At("HEAD"), cancellable = true)
  private static void hideHud(GuiGraphics context, float tickDelta, CallbackInfo ci) {
    StaminaConfig config = Common.getConfig();
    if (config.combatRollCompat && config.hideCombatRollHud) {
      ci.cancel();
    }
  }
}
