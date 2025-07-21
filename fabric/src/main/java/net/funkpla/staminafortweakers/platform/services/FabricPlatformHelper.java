package net.funkpla.staminafortweakers.platform.services;

import net.fabricmc.loader.api.FabricLoader;
import net.funkpla.staminafortweakers.platform.services.IPlatformHelper;
import net.minecraft.server.level.ServerPlayerGameMode;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDestroyingBlock(ServerPlayerGameMode gameMode) {
        return gameMode.isDestroyingBlock;
    }
}
