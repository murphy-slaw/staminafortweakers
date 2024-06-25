package net.funkpla.staminafortweakers.util;

public class Timer {
    private final int tickCount;
    private int remainingTicks;

    public Timer(int tickCount) {
        this.tickCount = tickCount;
        remainingTicks = tickCount;
    }

    public boolean expired() {
        return remainingTicks <= 0;
    }

    public void reset() {
        remainingTicks = tickCount;
    }

    public void tickDown() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }
}

