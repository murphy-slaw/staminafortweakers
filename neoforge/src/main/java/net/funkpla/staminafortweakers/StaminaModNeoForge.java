package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.PotionRecipes;
import net.funkpla.staminafortweakers.registry.Potions;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class StaminaModNeoForge {

  public StaminaModNeoForge(IEventBus modBus) {
    IEventBus eventBus = NeoForge.EVENT_BUS;
    Attributes.ATTRIBUTES.register(modBus);
    StatusEffects.MOB_EFFECTS.register(modBus);
    Potions.POTIONS.register(modBus);
    modBus.register(Attributes.class);
    modBus.addListener(PacketHandler::registerPackets);
    eventBus.register(PotionRecipes.class);
    modBus.addListener(StaminaModNeoForge::commonSetup);
  }

  @SubscribeEvent
  public static void commonSetup(FMLCommonSetupEvent event) {
    Common.initConfig();
  }
}
