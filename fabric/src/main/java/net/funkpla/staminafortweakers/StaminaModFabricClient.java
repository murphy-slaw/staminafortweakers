package net.funkpla.staminafortweakers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class StaminaModFabricClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientLifecycleEvents.CLIENT_STARTED.register(
        client -> HudRenderCallback.EVENT.register(new StaminaHudOverlay()));

    IconManagerFabric.init();
    ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        .registerReloadListener(new IconManagerFabric());

    FabricLoader.getInstance()
        .getModContainer(Constants.MOD_ID)
        .ifPresent(
            container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                    Common.locate("old_style_icons"),
                    container,
                    Component.literal("Old Style Stamina Icons"),
                    ResourcePackActivationType.NORMAL));
  }

  private static class IconManagerFabric extends IconManager
      implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
      return Common.locate("resources");
    }
  }
}
