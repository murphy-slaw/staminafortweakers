package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "staminafortweakers")
public class StaminaConfig implements ConfigData {
    public enum Orientation {
        Horizontal,
        Vertical
    }

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

    @Comment("Does jumping cost stamina?")
    @ConfigEntry.Category("Exhaustion")
    public boolean jumpingCostsStamina = true;

    @Comment("Stamina points depleted per jump")
    @ConfigEntry.Category("Exhaustion")
    public float depletionPerJump = 4.0F;

    @Comment("Can jump while exhausted")
    @ConfigEntry.Category("Exhaustion")
    public boolean canJumpWhileExhausted = true;

    @Comment("Exhaustion causes Darkness")
    @ConfigEntry.Category("Exhaustion")
    public boolean exhaustionBlackout = true;

    @Comment("Stamina points recovered per tick.")
    @ConfigEntry.Category("Recovery")
    public float recoveryPerTick = 0.25F;

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

    @Comment("HUD offset from center of screen")
    @ConfigEntry.Category("HUD")
    public int hudOffsetX = 91;

    @Comment("HUD offset from bottom of screen")
    @ConfigEntry.Category("HUD")
    public int hudOffsetY = 41;

    @Comment("Width of stamina bar")
    @ConfigEntry.Category("HUD")
    public int hudWidth = 81;

    @Comment("Height of stamina bar")
    @ConfigEntry.Category("HUD")
    public int hudHeight = 6;

    @Comment("Stamina bar orientation")
    @ConfigEntry.Category("HUD")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public Orientation orientation = Orientation.Horizontal;

    @Comment("Stamina bar main color")
    @ConfigEntry.ColorPicker
    @ConfigEntry.Category("Colors")
    public int staminaBarColor = 0x00FF00;

    @Comment("Stamina bar background color")
    @ConfigEntry.ColorPicker
    @ConfigEntry.Category("Colors")
    public int staminaBarBackgroundColor = 0x000000;

    @Comment("Stamina bar outline color")
    @ConfigEntry.ColorPicker
    @ConfigEntry.Category("Colors")
    public int staminaBarOutlineColor = 0xFFFFFF;

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
}
