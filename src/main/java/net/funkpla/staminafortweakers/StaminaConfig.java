package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@Config(name = "stamina-for-tweakers")
public class StaminaConfig implements ConfigData {
    @Comment("Percentage of total stamina triggering exhaustion")
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int exhaustedPercentage = 0;

    @Comment("Percentage of total stamina making the player winded")
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int windedPercentage = 25;

    @Comment("Percentage of total stamina making the player winded")
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int fatiguedPercentage = 50;

    @Comment("Stamina points recovered per tick.")
    public float recoveryPerTick = 0.05F;

    @Comment("Stamina points depleted per tick.")
    public float depletionPerTick = 0.1F;

    @Comment("Do players recover stamina while walking?")
    public boolean recoverWhileWalking;

    @Comment("Do players recover stamina when hungry?")
    public boolean recoverWhenHungry;

    @Comment("Do players recover stamina when not on the ground?")
    public boolean recoverWhenAirborne;
}
}
