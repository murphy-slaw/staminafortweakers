package net.funkpla.staminafortweakers;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.funkpla.staminafortweakers.client.StaminaHudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class StaminaHudOverlay implements HudRenderCallback {
  StaminaHudRenderer renderer = new StaminaHudRenderer();

  @Override
  public void onHudRender(GuiGraphics context, DeltaTracker deltaTracker) {
    Minecraft client = Minecraft.getInstance();
    renderer.renderHud(context, client);
  }
}
