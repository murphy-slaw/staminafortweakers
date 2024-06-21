package net.funkpla.staminafortweakers;

public class RecoveryDelayTimer {
    private int recoveryDelay;
    private int recoveryDelayRemaining;

    public RecoveryDelayTimer(int delay) {
        recoveryDelay = delay;
        recoveryDelayRemaining = delay;
    }

    public boolean expired() {
        return recoveryDelayRemaining <= 0;
    }

    public void reset() {
        recoveryDelayRemaining = recoveryDelay;
    }

    public void tickDown() {
        if (recoveryDelayRemaining > 0) {
            recoveryDelayRemaining--;
        }
    }
}

