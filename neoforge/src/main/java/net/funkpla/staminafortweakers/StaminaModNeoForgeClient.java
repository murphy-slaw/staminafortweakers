package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class StaminaModNeoForgeClient {
  public StaminaModNeoForgeClient(IEventBus modBus) {
    ModLoadingContext.get()
        .registerExtensionPoint(IConfigScreenFactory.class, () -> this::createScreen);

    var resManager = Minecraft.getInstance().getResourceManager();
    if (resManager instanceof ReloadableResourceManager) {
      ((ReloadableResourceManager) resManager).registerReloadListener(new IconManager());
      modBus.register(HudOverlayHandler.class);
      modBus.register(this);
    }
  }

  @SubscribeEvent
  public void addPackFinders(AddPackFindersEvent event) {
    event.addPackFinders(
        Common.locate("resourcepacks/old_style_icons"),
        PackType.CLIENT_RESOURCES,
        Component.literal("Old Style Stamina Icons"),
        PackSource.BUILT_IN,
        false,
        Pack.Position.BOTTOM);
  }

  private Screen createScreen(ModContainer modContainer, Screen parent) {
    return AutoConfig.getConfigScreen(StaminaConfig.class, parent).get();
  }
}
