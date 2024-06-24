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
import org.joml.Vector2i;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StaminaForTweakersClient implements ClientModInitializer {
    private static final HashMap<ResourceLocation, Vector2i> ICON_SIZES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        Minecraft client = Minecraft.getInstance();
        HudRenderCallback.EVENT.register(new StaminaHudOverlay());

        ArrayList<ResourceLocation> icons = new ArrayList<>();
        icons.add(StaminaForTweakers.resourceLocationOf("textures/stamina/walk.png"));
        icons.add(StaminaForTweakers.resourceLocationOf("textures/stamina/sprint.png"));

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation(StaminaForTweakers.MOD_ID, "resources");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                for (ResourceLocation icon : icons) {
                    SimpleTexture customTexture = (SimpleTexture) client.getTextureManager().getTexture(icon);
                    try {
                        Resource resource = resourceManager.getResourceOrThrow(icon);
                        try (InputStream inputStream = resource.open()) {
                            NativeImage nativeImage = NativeImage.read(inputStream);
                            ICON_SIZES.put(icon, new Vector2i(nativeImage.getWidth(), nativeImage.getHeight()));
                        } catch (IOException e) {
                            StaminaForTweakers.LOGGER.warn("Unable to read icon texture {}", icon);
                        }
                    } catch (FileNotFoundException e) {
                        StaminaForTweakers.LOGGER.warn("Icon texture '{}' not found", icon);
                    }
                }
            }
        });

    }

    public static Vector2i getIconSize(ResourceLocation rl) {
        return ICON_SIZES.get(rl);
    }


}