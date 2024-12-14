# Changelog for [Stamina for Tweakers](https://github.com/murphy-slaw/staminafortweakers)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] 2024-12-13

### Added

- Now exhaustion effects are completely configurable. The default configuration remains the same but you can replace, remove, or add any mob effect from Minecraft or any installed mod.
- Set duration and level for each effect.

### Changed

- Configuration flags related to enabling effects have been removed.

## [1.3.5] 2024-11-23

### Fixed

- Fixed logic in updateStamina() so that depletion for sprinting and jumping aren't mutually exclusive.

### Added

- Enchantments now have descriptions

## [1.3.4] 2024-10-22

### Changed

- Players are only considered to be wading if they are moving voluntarily in water. This means that you can recover stamina if you are being passively pushed by water.
- Depth Strider now reduces stamina depletion for swimming in the same way that Traveling reduces it for sprinting.
### Fixed
- Holding shift while climbing no longer disables stamina depletion

## [1.3.3] 2024-09-21

### Changed

- Wading (moving in or underwater but not swimming) now incurs the same stamina cost as swimming.

## [1.3.2] 2025-07-13

### Added

- Recipes for long potions of Fatigue and Tirelessness

### Fixed

- Fixed broken potion recipe registration on 1.21
- Added missing translation strings for Splash Potions, Lingering Potions, and Tipped Arrows

## [1.3.1] 2024-07-11

### Fixed

- Added missing translation string for Tirelessness effect

## [1.3.0] 2024-07-04

### Added

- Optional stamina cost for each block broken.
- New "Untiring" enchantment for tools
  - Reduces stamina depletion for ticks spent mining and blocks broken.
  - Three levels, completely eliminates stamina costs for mining at level III.
- Option to *increase* stamina costs for mining for each level of Efficiency. (Disabled by default.)
- Optional recovery cooldown after any stamina depletion (Disabled by default.)
  - This is in addition to the optional cooldown after exhaustion.

## [1.2.3] 2024-07-02

### Fixed

- Stamina HUD element now hides when hideGui is toggled with F1. (#17)

## [1.2.2] 2024-06-30

### Fixed

- Swimming upward (jumping in water) now consumes stamina.
- Cleaned up a bunch of ugly code.

### Changed

- Changed some default settings for a better out-of-the-box experience.
  - Doubled default recovery.
  - Reduced exhaustion cost for swimming by half.
  - Disabled the "fatigued" exhaustion tier - players are unaffected by fatigue until they become "winded" at 25%
  - Enabled a two-second recovery cooldown on exhaustion
  - Made the logarithmic recovery function the default.

## [1.2.1] 2024-06-25

Have little a bugfix, as a treat.

### Fixed

Fixed an error introduced when switching from Yarn to Mojang mappings, which caused the player to start sprinting
on a single tap of the `w` key. (#15)

## [1.2.0] 2024-06-25

A nice update to the HUD, and some tweaks.

### Added

- Option to display stamina in the hud as an icon (that also indicates sprint status)
- Better control over placement of HUD element
- Icon texture can be overridden by resource packs by overriding:

 ```
assets/staminafortweakers/textures/stamina/sprint.png
assets/staminafortweakers/textures/stamina/sprint_background.png
assets/staminafortweakers/textures/stamina/walk.png
assets/staminafortweakers/textures/stamina/walk_background.png
```

### Changed

- Added a small cooldown on stamina recovery while mining to keep the stamina display from "bouncing"
- Tweaked exhaustion effects slightly - now Fatigued only inflicts Fatigue I (was previously Fatigue II)
- Added a minimum recovery factor to the Logarithmic recovery formula to make waiting for the last bit of stamina less
  frustrating
- Extensively reorganized the codebase in ways that should have no effect on behavior, I hope.

## [1.1.1] 2024-06-23

Well, this is embarassing.

### Fixed

- Fixed stamina depleting while riding animals or "sprinting" in boats. (#9)

## [1.1.0] 2024-06-23

A magical update!

### Added

- Added two new potions: Fatigue and Tirelessness
    - Fatigue gives the Fatigue effect and is brewed with a Thick Potion and a Clay Ball.
    - Tirelessness completely prevents stamina depletion and is brewed with a Thick Potion and a Cocoa Bean.
- Added a new enchantment for Leggings, Traveling
    - Reduces stamina depletion from sprinting and jumping.
    - Three levels, at level III completely prevents stamina depletion from sprinting and jumping.

## [1.0.9] 2024-06-21

### Fixed

- Fixed issue where modded tools that break multiple blocks would prevent stamina recovery after breaking a block (#7)

## [1.0.8] 2024-06-20

### Added

- New option to toggle stamina recovery when suffocating
- Low stamina applies a new effect, "Fatigue" instead of Slowness
- Fatigue affects movement speed, climbing speed, and swimming speed.
- New option for mining to cost stamina.
- New option for exhaustion to cause Mining Fatigue.
- Optional delay before recovering from exhaustion.

## [1.0.7] 2024-06-20

### Fixed

Disable stamina for Creative and Spectator game modes.

## [1.0.6] 2024-06-19

### Fixed

Players no longer make breathing noises when winded underwater.

## [1.0.5] 2024-06-19

### Changed
- Added configurable stamina depletion for climbing.
- Added somewhat upsetting sounds when the player is winded.

## [1.0.4] 2024-06-18

### Changed

- Moved most of the player stamina code back to the server side, because apparently I have no idea what I'm doing.

### Fixed

- Slowness from exhaustion works again.

## [1.0.3] 2024-06-16

### Changed

- Rearranged the guts of all of the mixins for sanity

### Fixed
- Now all stamina attribute updates are in the ClientPlayerEntity, which fixes some issues where the server and the client got out of sync because they were both updating the attribute. Oops.

## [1.0.2] 2024-06-16

### Changed

Refactored to do stamina updates in PlayerEntity.tick() instead of endWorldTick.

## [1.0.1] 2024-06-15

### Fixed
- Fixed recoverWhileWalking config not working.

### Changed
- Renamed references to stamina bar "height" and "length" to "long side" and "short side" since it can be oriented two ways.

## [1.0.0] 2024-06-15

Initial public release.
