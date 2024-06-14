package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "staminafortweakers")
public class StaminaConfig implements ConfigData {
    @Comment("Percentage of total stamina triggering exhaustion")
    @ConfigEntry.BoundedDiscrete(max = 100)
    @ConfigEntry.Category("Exhaustion")
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
    @Comment("Stamina points depleted per tick.")
    public float depletionPerTick = 0.1F;

    @Comment("Does jumping cost stamina?")
    @ConfigEntry.Category("Exhaustion")
    public boolean jumpingCostsStamina = true;

    @Comment("Can jump while exhausted")
    @ConfigEntry.Category("Exhaustion")
    public boolean canJumpWhileExhausted = true;

    @Comment("Exhaustion really sucks")
    @ConfigEntry.Category("Exhaustion")
    public boolean exhaustionSucksBad = true;

    @Comment("Stamina points recovered per tick.")
    @ConfigEntry.Category("Recovery")
    public float recoveryPerTick = 0.05F;

    @Comment("Do players recover stamina while walking?")
    @ConfigEntry.Category("Recovery")
    public boolean recoverWhileWalking = true;

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
}
