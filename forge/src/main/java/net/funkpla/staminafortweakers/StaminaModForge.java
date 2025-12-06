package net.funkpla.staminafortweakers;

import java.nio.file.Path;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.funkpla.staminafortweakers.registry.Enchantments;
import net.funkpla.staminafortweakers.registry.Potions;
import net.funkpla.staminafortweakers.registry.StatusEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class StaminaModForge {

  public StaminaModForge() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    StatusEffects.registerStatusEffects();
    Attributes.registerAttributes();
    PacketHandler.registerPackets();
    Potions.registerPotions();
    Enchantments.registerEnchantments();
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
    modBus.addListener(Attributes::addLivingEntityAttributes);
    modBus.addListener(StaminaModForge::addPackFinders);
    modBus.addListener(StaminaModForge::commonSetup);
  }

  @SubscribeEvent
  public static void commonSetup(FMLCommonSetupEvent event) {
    Common.initConfig();
  }

  @SubscribeEvent
  public static void addPackFinders(AddPackFindersEvent event) {
    if (event.getPackType() == PackType.CLIENT_RESOURCES) {
      Path resourcePath =
          ModList.get()
              .getModFileById(Constants.MOD_ID)
              .getFile()
              .findResource("resourcepacks/old_style_icons");
      Pack pack =
          Pack.readMetaAndCreate(
              "Old Style Icons",
              Component.literal("Old Style Icons"),
              false,
              (path) -> new PathPackResources(path, resourcePath, false),
              PackType.CLIENT_RESOURCES,
              Pack.Position.BOTTOM,
              PackSource.BUILT_IN);
      event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
    }
  }
}
