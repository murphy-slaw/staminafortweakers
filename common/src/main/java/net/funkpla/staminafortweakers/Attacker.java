package net.funkpla.staminafortweakers;

public interface Attacker {
  void setAttacked(boolean attacked);

  boolean getAttacked();

  void setSwungWeapon(boolean swung);

  boolean getSwungWeapon();
}
