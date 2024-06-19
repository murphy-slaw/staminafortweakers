package net.funkpla.staminafortweakers;

import net.minecraft.util.math.Vec3d;

public interface Climber {
    Vec3d getClimbSpeed(Vec3d original);
}
