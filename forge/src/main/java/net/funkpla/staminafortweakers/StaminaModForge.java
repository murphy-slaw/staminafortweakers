package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class StaminaModForge {

  public StaminaModForge() {
    Common.init();
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    StatusEffects.registerStatusEffects();
    Attributes.registerAttributes();
    PacketHandler.registerPackets();
    Potions.registerPotions();
    Enchantments.registerEnchantments();
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
    modBus.addListener(Attributes::addLivingEntityAttributes);
  }

}
