package net.funkpla.staminafortweakers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.funkpla.staminafortweakers.packet.S2CReceivers;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class StaminaClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientLifecycleEvents.CLIENT_STARTED.register(client->{
      HudRenderCallback.EVENT.register(new StaminaHudOverlay());
    });
    S2CReceivers.registerS2CReceivers();
    IconManagerFabric.init();
    ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        .registerReloadListener(new IconManagerFabric());
  }

  private static class IconManagerFabric extends IconManager
      implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
      return new ResourceLocation(Constants.MOD_ID, "resources");
    }
  }

}
