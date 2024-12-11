package net.funkpla.staminafortweakers.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModIntegrations {
    public static final String FD_MOD_ID = "farmersdelight";
    public static final ResourceLocation FD_NOURISHED_EFFECT_NAME =  ResourceLocation.fromNamespaceAndPath(FD_MOD_ID, "nourishment");
    protected static final Map<String, Boolean> INTEGRATIONS = new HashMap<>();

    protected static void registerIntegration(String modId) {
        if (FabricLoader.getInstance().isModLoaded(modId)) {
            INTEGRATIONS.put(modId, true);
        }
    }

    public static Optional <Reference<MobEffect>> maybeNourished() {
        if (isIntegrationActive(FD_MOD_ID)) {
            return BuiltInRegistries.MOB_EFFECT.getHolder(FD_NOURISHED_EFFECT_NAME);
        }
        return Optional.empty();
    }

    public static void register() {
        registerIntegration(FD_MOD_ID);
    }

    public static boolean isIntegrationActive(String modId) {
        return INTEGRATIONS.containsKey(modId);
    }
}
