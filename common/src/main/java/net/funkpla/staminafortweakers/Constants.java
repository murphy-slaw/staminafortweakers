package net.funkpla.staminafortweakers;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
  public static final String MOD_ID = "staminafortweakers";
  public static final String MOD_NAME = "Stamina For Tweakers";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
  public static final TagKey<Item> MELEE_WEAPON =
      TagKey.create(Registries.ITEM, Common.locate("melee_weapon"));
  public static final TagKey<Item> SHIELDS =
      TagKey.create(Registries.ITEM, Common.locate("shield"));
}
