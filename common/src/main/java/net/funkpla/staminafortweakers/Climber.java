package net.funkpla.staminafortweakers;

import net.minecraft.world.phys.Vec3;

public interface Climber {
    Vec3 getClimbSpeed(Vec3 original);
}
