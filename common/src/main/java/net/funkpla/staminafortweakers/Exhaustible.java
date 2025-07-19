package net.funkpla.staminafortweakers;

public interface Exhaustible {
    boolean shouldExhaust();
    boolean isExhausted();
    boolean isWinded();
    boolean isShieldAllowed();
    void setShieldAllowed(boolean allowed);
}
