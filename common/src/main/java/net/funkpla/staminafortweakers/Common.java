package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraft.resources.ResourceLocation;

public class Common {
  public static void init() {
    AutoConfig.register(StaminaConfig.class, JanksonConfigSerializer::new);
  }

  public static ResourceLocation locate(String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}