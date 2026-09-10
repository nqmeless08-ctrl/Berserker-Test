# ClassSystem

An independent class system for your Paper server. Separate from Origins-Reborn —
players pick an Origin through Origins-Reborn as normal, then use `/class` to
open a second menu and pick a Class on top of it.

## Currently implemented

**Berserker** (Iron Sword icon)
> A blood-hungry warrior that doesn't think twice before stepping into battle.
- +3 Attack Damage
- -2 Max Health
- Permanent Speed I
- Cannot use a shield

## How it works

- `/class` opens the selection menu.
  - Java players get a chest-style inventory GUI.
  - Bedrock players (detected via Floodgate) get a native Bedrock form instead,
    same pattern Origins-Reborn itself uses for its Origin menu.
- A player's class is stored in their PersistentDataContainer (no database needed).
- `/setclass <player> <classid>` lets an op force a class (permission: `classsystem.admin`).
- Effects (attribute modifiers, potion effects) are reapplied on join and respawn,
  since potion effects clear on death and attribute modifiers can be lost across restarts.

## Building

You'll need Java 17+ and Maven installed locally.

```
mvn clean package
```

The compiled plugin will be at `target/ClassSystem.jar`. Drop it into your
server's `plugins/` folder alongside Paper and (optionally) Floodgate/Geyser.

**Note on dependencies:** this was written without a live Maven build against
the real Paper/Floodgate/Cumulus jars (see limitations discussed in chat), so
after your first `mvn clean package`, if you get dependency-resolution or
compile errors, send me the exact error and I'll fix the code — this is the
normal first-pass iteration loop for any code that can't be run in advance.
Two spots most likely to need adjustment:
- The `paper-api` version in `pom.xml` (`1.21.1-R0.1-SNAPSHOT`) — bump it to
  match your server's exact version if needed.
- The Cumulus form-builder method chain in `BedrockClassForm.java` — Floodgate's
  form API has shifted slightly between versions; the shape is stable but a
  method name or two may need tweaking for your exact Floodgate version.

## Adding more classes

1. Create a new class in `classes/` implementing `PlayerClass` (copy
   `BerserkerClass.java` as a template).
2. Register it in `ClassSystemPlugin.onEnable()`:
   ```java
   classRegistry.register(new YourNewClass());
   ```
That's it — it'll automatically show up in both the Java GUI and the Bedrock form.

## Requirements

- Paper (or Purpur) 1.21.x
- Optional: Floodgate + Geyser, for the Bedrock-native form. Without them,
  Bedrock players (connecting via Geyser only) will just see the Java GUI
  rendered through Geyser's normal inventory translation, which still works.
