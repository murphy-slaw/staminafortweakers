package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "staminafortweakers")
public class StaminaConfig implements ConfigData {

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

    /*
     * Exhaustion rules
     */

    @Comment("Stamina points depleted per attack")
    @ConfigEntry.Category("Exhaustion")
    public float depletionPerAttack = 1F;

    @Comment("Exhaustion slows mining")
    @ConfigEntry.Category("Exhaustion")
    public boolean exhaustionSlowsMining = false;

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
    public int fatiguedPercentage = 50;

    @ConfigEntry.Category("Exhaustion")
    @Comment("Stamina points depleted per tick while sprinting.")
    public float depletionPerTickSprinting = 1F;

    @ConfigEntry.Category("Exhaustion")
    @Comment("Stamina points depleted per tick while swimming.")
    public float depletionPerTickSwimming = 1F;

    @ConfigEntry.Category("Exhaustion")
    @Comment("Stamina points depleted per tick while climbing.")
    public float depletionPerTickClimbing = 1F;

    @Comment("Stamina points depleted per jump")
    @ConfigEntry.Category("Exhaustion")
    public float depletionPerJump = 4.0F;

    @Comment("Can jump while exhausted")
    @ConfigEntry.Category("Exhaustion")
    public boolean canJumpWhileExhausted = true;

    @Comment("Exhaustion causes Darkness")
    @ConfigEntry.Category("Exhaustion")
    public boolean exhaustionBlackout = true;

    @Comment("Exhaustion sounds")
    @ConfigEntry.Category("Exhaustion")
    public boolean exhaustionSounds = true;

    /*
     * Recovery rules
     */

    @Comment("% of max stamina recovered per tick.")
    @ConfigEntry.Category("Recovery")
    public float recoveryPerTick = 0.25F;

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
    public int recoveryDelayTicks = 0;

    @Comment("Which formula to use for recovery")
    @ConfigEntry.Category("Recovery")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public Formula formula = Formula.LINEAR;

    /*
     * Stamina bar configuration
     */

    @Comment("HUD vertical alignment")
    @ConfigEntry.Category("HUD")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public AlignmentV alignV = AlignmentV.BOTTOM;

    @Comment("HUD horizontal alignment")
    @ConfigEntry.Category("HUD")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public AlignmentH alignH = AlignmentH.CENTER;

    @Comment("HUD vertical offset")
    @ConfigEntry.Category("HUD")
    public int hudOffsetX = 51;

    @Comment("HUD horizontal offset")
    @ConfigEntry.Category("HUD")
    public int hudOffsetY = 41;

    @Comment("Width of stamina bar")
    @ConfigEntry.Category("HUD")
    public int hudLongSide = 81;

    @Comment("Height of stamina bar")
    @ConfigEntry.Category("HUD")
    public int hudShortSide = 6;

    @Comment("Stamina bar orientation")
    @ConfigEntry.Category("HUD")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public Orientation orientation = Orientation.HORIZONTAL;

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
}
