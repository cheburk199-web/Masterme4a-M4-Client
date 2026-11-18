# Masterme4a Item Scroller

Minecraft **1.17.1**, Forge **37.1.1**, Java **16**.

## Feature

While a chest, shulker, or another container GUI is open, **left-click an occupied slot** to immediately quick-move its stack to the other inventory. You do not need to hold Shift.

The mod sends the normal vanilla `QUICK_MOVE` inventory action. The server remains authoritative and can reject actions according to its inventory rules or anti-cheat. No custom packets, duplication glitches, or anti-cheat bypasses are used.

## Build

Run `gradle build` with Gradle installed, or import this project into IntelliJ IDEA and let Gradle sync. Use JDK 16. The built JAR will be at `build/libs/Masterme4a-ItemScroller-1.0.0.jar`.

## Install

Put the built JAR in the client's `.minecraft/mods` folder with Forge 1.17.1. No server-side installation is required.
