package net.funkpla.staminafortweakers;


import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ClientSetup {
  public ClientSetup() {
    HudOverlayHandler.init();
    ModLoadingContext.get()
        .registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () ->
                new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) ->
                        AutoConfig.getConfigScreen(StaminaConfig.class, parent).get()));

    var resManager = Minecraft.getInstance().getResourceManager();
    if (resManager instanceof ReloadableResourceManager) {
      ((ReloadableResourceManager) resManager).registerReloadListener(new IconManager());
    }
  }
}
