package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.client.StaminaHudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HudOverlayHandler {

  static final StaminaHudRenderer renderer = new StaminaHudRenderer();

  @SubscribeEvent
  public static void register(RegisterGuiLayersEvent event) {
    IconManager.init();
    event.registerAbove(
        VanillaGuiLayers.EXPERIENCE_BAR,
        ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stamina"),
        new StaminaOverlay());
  }

  public static class StaminaOverlay implements LayeredDraw.Layer {
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
      renderer.renderHud(guiGraphics, Minecraft.getInstance());
    }
  }
}
