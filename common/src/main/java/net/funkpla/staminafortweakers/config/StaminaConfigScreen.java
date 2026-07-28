package net.funkpla.staminafortweakers.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StaminaConfigScreen {
  public static Screen create(Screen parent) {
    var provider =
        (ConfigScreenProvider<StaminaConfig>)
            AutoConfig.getConfigScreen(StaminaConfig.class, parent);
    provider.setBuildFunction(
        builder -> {
          if (!Services.PLATFORM.isModLoaded("combat_roll")) {
            builder.removeCategoryIfExists(
                Component.translatable("text.autoconfig.staminafortweakers.category.CombatRoll"));
          }
          return builder.build();
        });
    return provider.get();
  }
}
