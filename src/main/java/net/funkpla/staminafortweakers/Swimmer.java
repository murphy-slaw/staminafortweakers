package net.funkpla.staminafortweakers;

public interface Swimmer {
    boolean swamUp();
    boolean hasMovementInput();

    void setSwamUp(boolean b);
    void setHasMovementInput(boolean b);
}