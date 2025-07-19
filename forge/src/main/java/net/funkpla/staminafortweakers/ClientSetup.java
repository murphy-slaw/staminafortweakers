package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ClientSetup {
    public ClientSetup() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> AutoConfig.getConfigScreen(StaminaConfig.class, parent).get()
                )
        );
    }

}