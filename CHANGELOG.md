# Changelog for [Stamina for Tweakers](https://github.com/murphy-slaw/staminafortweakers)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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