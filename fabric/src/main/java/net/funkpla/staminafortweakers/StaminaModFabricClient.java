package net.funkpla.staminafortweakers;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StaminaModFabricClient implements ClientModInitializer {
    private static final HashMap<ResourceLocation, Vector2i> ICON_SIZES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new StaminaHudOverlay());

        ArrayList<ResourceLocation> icons = new ArrayList<>();
        icons.add(Common.locate("textures/stamina/walk.png"));
        icons.add(Common.locate("textures/stamina/sprint.png"));

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "resources");
            }

            @Override
            public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
                for (ResourceLocation icon : icons) {
                    try {
                        Resource resource = resourceManager.getResourceOrThrow(icon);
                        try (InputStream inputStream = resource.open()) {
                            NativeImage nativeImage = NativeImage.read(inputStream);
                            ICON_SIZES.put(icon, new Vector2i(nativeImage.getWidth(), nativeImage.getHeight()));
                        } catch (IOException e) {
                            Constants.LOG.warn("Unable to read icon texture {}", icon);
                        }
                    } catch (FileNotFoundException e) {
                        Constants.LOG.warn("Icon texture '{}' not found", icon);
                    }
                }
            }
        });
    }

    public static Vector2i getIconSize(ResourceLocation rl) {
        return ICON_SIZES.get(rl);
    }


}