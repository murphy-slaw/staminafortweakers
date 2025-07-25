package net.funkpla.staminafortweakers.platform.services;

import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "Forge";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return ModList.get().isLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return !FMLLoader.isProduction();
  }

  @Override
  public boolean isDestroyingBlock(ServerPlayerGameMode gameMode) {
    return gameMode.isDestroyingBlock;
  }
}
