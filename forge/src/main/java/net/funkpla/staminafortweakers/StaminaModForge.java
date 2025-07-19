package net.funkpla.staminafortweakers;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(StaminaConstants.MOD_ID)
public class StaminaModForge {

    public StaminaModForge() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        StaminaConstants.LOG.info("Hello Forge world!");
        StaminaCommon.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::onAttachingCapabilities);
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
    }

    @SubscribeEvent
    public void onAttachingCapabilities(final AttachCapabilitiesEvent<Entity> event) {
    }
}