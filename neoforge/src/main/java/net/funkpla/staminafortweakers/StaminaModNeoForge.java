package net.funkpla.staminafortweakers;

import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.w3c.dom.Attr;

@Mod(Constants.MOD_ID)
public class StaminaModNeoForge {

  public StaminaModNeoForge(IEventBus modBus) {
    Common.init();
    Attributes.ATTRIBUTES.register(modBus);
    StatusEffects.MOB_EFFECTS.register(modBus);
    modBus.register(Attributes.class);
    modBus.addListener(PacketHandler::registerPackets);
  }
}
