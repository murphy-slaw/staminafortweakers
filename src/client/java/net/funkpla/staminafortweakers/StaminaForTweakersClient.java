package net.funkpla.staminafortweakers;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StaminaForTweakersClient implements ClientModInitializer {
    public static final ResourceLocation CUSTOM_TEXTURE = new ResourceLocation(StaminaForTweakers.MOD_ID, "textures/stamina/filled_stamina.png");
    private static int CUSTOM_HEIGHT;
    private static int CUSTOM_WIDTH;


    @Override
    public void onInitializeClient() {
        Minecraft client = Minecraft.getInstance();
        HudRenderCallback.EVENT.register(new StaminaHudOverlay());

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(StaminaForTweakers.MOD_ID, "resources");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                SimpleTexture customTexture = (SimpleTexture) client.getTextureManager().getTexture(CUSTOM_TEXTURE);

                try {
                    Resource resource = resourceManager.getResourceOrThrow(CUSTOM_TEXTURE);
                    try (InputStream inputStream = resource.open();) {
                        NativeImage nativeImage = NativeImage.read(inputStream);
                        CUSTOM_HEIGHT = nativeImage.getHeight();
                        CUSTOM_WIDTH = nativeImage.getWidth();
                    } catch (IOException e) {
                    }
                } catch (FileNotFoundException e) {
                }
            }
        });

    }

    public static int getCustomHeight() {
        return CUSTOM_HEIGHT;
    }

    public static int getCustomWidth() {
        return CUSTOM_WIDTH;
    }
}