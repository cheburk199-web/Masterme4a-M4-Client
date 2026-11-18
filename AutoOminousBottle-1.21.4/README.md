# Masterme4a Auto Ominous Bottle

Client-side Fabric mod for Minecraft 1.21.4.

## Behavior
- Starts disabled.
- `/autobottle on` enables automatic use.
- `/autobottle off` disables it.
- `/autobottle status` displays the current state.
- When enabled, searches the hotbar (slots 1–9) for an Ominous Bottle and drinks it if Bad Omen is not already active.
- Temporarily selects the bottle's hotbar slot. The previously selected slot is restored when no bottle is available or Bad Omen is active.

The mod only sends the normal vanilla item-use interaction. It does not add server-side code or bypass server rules. The bottle must be in the hotbar.

## Build
Requires Java 21 and Gradle 8. Use `gradle build`. The JAR is in `build/libs/`.
