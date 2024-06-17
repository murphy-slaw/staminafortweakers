# Changelog for [Stamina for Tweakers](https://github.com/murphy-slaw/staminafortweakers)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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