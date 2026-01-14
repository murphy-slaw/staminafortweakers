package net.funkpla.staminafortweakers.config;

import java.util.ArrayList;
import java.util.List;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.funkpla.staminafortweakers.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("CanBeFinal")
@Config(name = Constants.MOD_ID)
public class StaminaConfig implements ConfigData {

  @Comment("Percentage of total stamina triggering exhaustion")
  @ConfigEntry.BoundedDiscrete(max = 100)
  @ConfigEntry.Category("Exhaustion")
  @ConfigEntry.Gui.Excluded()
  public int exhaustedPercentage = 0;

  @Comment("Percentage of total stamina making the player winded")
  @ConfigEntry.BoundedDiscrete(max = 100)
  @ConfigEntry.Category("Exhaustion")
  public int windedPercentage = 25;

  @Comment("Percentage of total stamina making the player fatigued")
  @ConfigEntry.BoundedDiscrete(max = 100)
  @ConfigEntry.Category("Exhaustion")
  public int fatiguedPercentage = 0;

  @ConfigEntry.Category("Exhaustion")
  @Comment("Stamina points depleted per tick while sprinting.")
  public float depletionPerTickSprinting = 1F;

  @ConfigEntry.Category("Exhaustion")
  @Comment("Stamina points depleted per tick while swimming.")
  public float depletionPerTickSwimming = 0.5F;

  @ConfigEntry.Category("Exhaustion")
  @Comment("Stamina points depleted per tick while moving in water.")
  public float depletionPerTickWading = 0.25F;

  /*
   * Exhaustion rules
   */
  @ConfigEntry.Category("Exhaustion")
  @Comment("Stamina points depleted per tick while climbing.")
  public float depletionPerTickClimbing = 1F;

  @Comment("Stamina points depleted per jump")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerJump = 4.0F;

  @Comment("Stamina points depleted per attack")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerAttack = 0F;

  @Comment("Stamina points depleted per weapon swing")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerWeaponSwing = 0F;

  @Comment("Stamina points depleted per tick while mining")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerMiningTick = 1F;

  @Comment("Stamina points depleted per block broken")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerBlockBroken = 4F;

  @Comment("Stamina points depleted per tick while using a shield")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerShieldTick = 0;

  @Comment("Stamina points depleted per tick while using a glider")
  @ConfigEntry.Category("Exhaustion")
  public float depletionPerGliderTick = 0;

  @Comment("Can jump while exhausted")
  @ConfigEntry.Category("Exhaustion")
  public boolean canJumpWhileExhausted = true;

  @Comment("Exhaustion sounds")
  @ConfigEntry.Category("Exhaustion")
  public boolean exhaustionSounds = true;

  @Comment("Efficiency increases exhaustion")
  @ConfigEntry.Category("Exhaustion")
  public boolean efficiencyExhausts = true;

  /*
   * Recovery rules
   */

  @Comment("% of max stamina recovered per tick.")
  @ConfigEntry.Category("Recovery")
  public float recoveryPerTick = 0.5F;

  @Comment("Bonus recovery multiplier for not moving.")
  @ConfigEntry.Category("Recovery")
  public float recoveryRestBonusMultiplier = 2;

  @Comment("Do players recover stamina while walking?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverWhileWalking = false;

  @Comment("Do players recover stamina while sneaking?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverWhileSneaking = true;

  @Comment("Do players recover stamina when too hungry to sprint?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverWhenHungry = true;

  @Comment("Do players recover stamina when not on the ground?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverWhileAirborne = true;

  @Comment("Do players recover stamina when submerged?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverUnderwater = true;

  @Comment("Do players recover stamina when suffocating?")
  @ConfigEntry.Category("Recovery")
  public boolean recoverWhileBreathless = false;

  @Comment("Delay in ticks before starting recovery from exhaustion")
  @ConfigEntry.BoundedDiscrete(max = 600)
  @ConfigEntry.Category("Recovery")
  public int recoveryExhaustDelayTicks = 40;

  @Comment("Delay in ticks before starting recovery after depletion")
  @ConfigEntry.BoundedDiscrete(max = 600)
  @ConfigEntry.Category("Recovery")
  public int recoveryDelayTicks = 0;

  @Comment("Delay in ticks before regaining shield use")
  @ConfigEntry.BoundedDiscrete(max = 600)
  @ConfigEntry.Category("Recovery")
  public int shieldRecoveryDelayTicks = 40;

  @Comment("Do players fully recover stamina on respawn?")
  @ConfigEntry.Category("Recovery")
  @ConfigEntry.Gui.Excluded
  public boolean fullRecoveryOnRespawn = true;

  @Comment("Which formula to use for recovery")
  @ConfigEntry.Category("Recovery")
  @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
  public Formula formula = Formula.LOGARITHMIC;

  @Comment("HUD vertical alignment")
  @ConfigEntry.Category("HUD")
  @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
  public AlignmentV alignV = AlignmentV.BOTTOM;

  @Comment("HUD horizontal alignment")
  @ConfigEntry.Category("HUD")
  @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
  public AlignmentH alignH = AlignmentH.CENTER;

  @Comment("HUD Display Mode")
  @ConfigEntry.Category("HUD")
  @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
  public HudMode hudMode = HudMode.ICON;

  @Comment("Icon Settings")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
  @ConfigEntry.Category("HUD")
  public IconConfig icon = new IconConfig();

  @Comment("Stamina Bar Settings")
  @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
  @ConfigEntry.Category("HUD")
  public StaminaBarConfig bar = new StaminaBarConfig();

  /*
   * Stamina bar colors.
   */

  @Comment("Stamina bar background color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarBackgroundColor = 0x222222;

  @Comment("Stamina bar outline color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarOutlineColor = 0x000000;

  @Comment("Stamina bar main color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarColor = 0x00FF00;

  @Comment("Stamina bar fatigued color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarFatiguedColor = 0xFFFF00;

  @Comment("Stamina bar winded color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarWindedColor = 0xFF8000;

  @Comment("Stamina bar nearly exhausted color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarNearlyExhaustedColor = 0xFF0000;

  @Comment("Stamina bar Tireless effect color")
  @ConfigEntry.ColorPicker
  @ConfigEntry.Category("Colors")
  public int staminaBarTirelessColor = 0xFEB236;

  /*
  Customizable effects
   */

  @ConfigEntry.Gui.PrefixText
  @Comment("Exhaustion effects")
  @ConfigEntry.Category("Effects")
  public List<EffectConfig> exhaustedEffects = new ArrayList<>();

  @Comment("Winded effects")
  @ConfigEntry.Category("Effects")
  public List<EffectConfig> windedEffects = new ArrayList<>();

  @Comment("Fatigued effects")
  @ConfigEntry.Category("Effects")
  public List<EffectConfig> fatiguedEffects = new ArrayList<>();

  @Comment("Effects that prevent stamina depletion")
  @ConfigEntry.Category("Effects")
  public List<SimpleEffectConfig> untiringEquivalentEffects = new ArrayList<>();

  @ConfigEntry.Gui.PrefixText
  @Comment("Chance Traveling will damage armor each active tick")
  @ConfigEntry.Category("Enchantments")
  @ConfigEntry.BoundedDiscrete(max = 100)
  public int travelingDamageChance = 4;

  @ConfigEntry.Gui.PrefixText
  @ConfigEntry.Category("Enchantments")
  @Comment("Traveling minimum durability")
  public int travelingMinimumDurability = 0;

  private boolean effectIdMissing(INamedEffect e) {
    var b = BuiltInRegistries.MOB_EFFECT.containsKey(ResourceLocation.of(e.getId(), ':'));
    if (!b) {
      Constants.LOG.warn("Effect {} not found in registry, removing.", e.getId());
    }
    return !b;
  }

  private void validateEffectConfigs(List<EffectConfig> effects) {
    List<EffectConfig> missing = new ArrayList<>();
    for (EffectConfig e : effects) {
      if (effectIdMissing(e)) {
        missing.add(e);
      }
    }
    effects.removeAll(missing);
  }

  private void validateSimpleEffectConfigs(List<SimpleEffectConfig> effects) {
    List<SimpleEffectConfig> missing = new ArrayList<>();
    for (SimpleEffectConfig e : effects) {
      if (effectIdMissing(e)) {
        missing.add(e);
      }
    }
    effects.removeAll(missing);
  }

  public void validatePostLoad() {
    Constants.LOG.info("Validating Exhausted effects.");
    validateEffectConfigs(exhaustedEffects);
    Constants.LOG.info("Validating Winded effects.");
    validateEffectConfigs(windedEffects);
    Constants.LOG.info("Validating Fatigued effects.");
    validateEffectConfigs(fatiguedEffects);
    Constants.LOG.info("Validating Untiring equivalent effects.");
    validateSimpleEffectConfigs(untiringEquivalentEffects);
  }

  public enum AlignmentV {
    TOP,
    BOTTOM,
    CENTER,
  }

  public enum AlignmentH {
    LEFT,
    RIGHT,
    CENTER
  }

  public enum Orientation {
    HORIZONTAL,
    VERTICAL
  }

  public enum Formula {
    LINEAR,
    LOGARITHMIC
  }

  public enum HudMode {
    BAR,
    ICON
  }

  @SuppressWarnings("CanBeFinal")
  public static class IconConfig {
    @Comment("Hud Icon Height")
    @ConfigEntry.BoundedDiscrete(max = 320)
    public int height = 22;

    @Comment("Horizontal offset")
    public int offsetY = 1;

    @Comment("Vertical offset")
    public int offsetX = 105;
  }

  @SuppressWarnings("CanBeFinal")
  public static class StaminaBarConfig {
    @Comment("Stamina bar orientation")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public Orientation orientation = Orientation.HORIZONTAL;

    @Comment("Short side (pixels)")
    public int shortSide = 2;

    @Comment("Long side (pixels)")
    public int longSide = 180;

    @Comment("Horizontal offset")
    public int offsetY = 22;

    @Comment("Vertical offset")
    public int offsetX = 0;
  }
}
