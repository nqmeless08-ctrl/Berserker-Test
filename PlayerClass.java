package com.yourserver.classsystem.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Contract that every class in the system must implement.
 * Keeping this generic means adding a new class later is just
 * "write one file + register it" — nothing else needs to change.
 */
public interface PlayerClass {

    /** Unique lowercase id, used for storage and commands. e.g. "berserker" */
    String getId();

    /** Display name shown in menus. e.g. "Berserker" */
    String getDisplayName();

    /** Icon material shown in the GUI / used as a form image hint. */
    Material getIcon();

    /** Flavor text / description shown in the GUI lore and forms. */
    String getDescription();

    /** Short bullet list of stat changes, shown under the description. e.g. "+3 Attack Damage" */
    List<String> getStatLines();

    /**
     * Called when a player is granted this class (on selection, on join if already
     * selected, and on respawn to reapply effects that death/respawn resets).
     */
    void apply(Player player);

    /**
     * Called when a player is switching away from this class, so all
     * attribute modifiers / effects tied to it can be cleanly removed.
     */
    void remove(Player player);

    /**
     * Whether this class blocks shield usage. Checked by ShieldBlockListener.
     */
    default boolean blocksShieldUse() {
        return false;
    }
}
