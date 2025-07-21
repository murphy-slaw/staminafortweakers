package net.funkpla.staminafortweakers;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class IconManager implements ResourceManagerReloadListener {
  static final HashMap<ResourceLocation, Vector2i> ICON_SIZES = new HashMap<>();
  @Getter static final ArrayList<ResourceLocation> icons = new ArrayList<>();

  public static Vector2i getIconSize(ResourceLocation rl) {
    return ICON_SIZES.get(rl);
  }

  public static void init() {
    icons.add(Common.locate("textures/stamina/walk.png"));
    icons.add(Common.locate("textures/stamina/sprint.png"));
  }


  @Override
  public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
    for (ResourceLocation icon : getIcons()) {
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
}
