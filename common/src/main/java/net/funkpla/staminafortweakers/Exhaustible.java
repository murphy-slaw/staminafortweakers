package net.funkpla.staminafortweakers;

public interface Exhaustible {
  boolean shouldExhaust();

  boolean isExhausted();

  boolean isWinded();

  boolean shouldSprintExhaust();

  boolean shouldSwimExhaust();

  boolean shouldWadeExhaust();

  boolean shouldClimbExhaust();

  boolean hasJumped();

  boolean isMining();

  boolean getAttacked();

  boolean getSwungWeapon();

  boolean isUsingShield();

  void depleteStamina(float depletionAmount);

  void maybeDamageLeggings();

  float getMiningModifier();

  float getTravelingModifier();

  float getDepthStriderModifier();

  void resetStamina();

  default void handleRespawn() {}
}
