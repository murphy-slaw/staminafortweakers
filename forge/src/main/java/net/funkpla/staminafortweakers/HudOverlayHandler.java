package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.client.StaminaHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class HudOverlayHandler {

  static final ResourceLocation EXP_ELEMENT = new ResourceLocation("minecraft", "experience_bar");
  static final StaminaHudRenderer renderer = new StaminaHudRenderer();

  public static void init() {
    IconManager.init();
    MinecraftForge.EVENT_BUS.register(new HudOverlayHandler());
  }

  @SubscribeEvent
  public void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
    if (event.getOverlay() == GuiOverlayManager.findOverlay(EXP_ELEMENT)) {
      Minecraft mc = Minecraft.getInstance();
      ForgeGui gui = (ForgeGui) mc.gui;
      if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
        RenderEvent renderEvent = new RenderEvent(event.getGuiGraphics());
        MinecraftForge.EVENT_BUS.post(renderEvent);
        if (!renderEvent.isCanceled())
          renderer.renderHud(renderEvent.guiGraphics, event.getPartialTick(), mc);
      }
    }
  }

  private static class RenderEvent extends Event{
    GuiGraphics guiGraphics;

    private RenderEvent(GuiGraphics guiGraphics)
    {
      this.guiGraphics = guiGraphics;
    }
    @Override
    public boolean isCancelable()
    {
      return true;
    }
  }
}
