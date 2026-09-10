package com.yourserver.classsystem.classes;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * Handles storing which class a player has chosen (via PersistentDataContainer,
 * so it survives restarts without needing a database) and applying/removing
 * the effects tied to that class.
 */
public class ClassManager {

    private final Plugin plugin;
    private final ClassRegistry registry;
    private final NamespacedKey classKey;

    public ClassManager(Plugin plugin, ClassRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
        this.classKey = new NamespacedKey(plugin, "player_class");
    }

    public Optional<PlayerClass> getClass(Player player) {
        String id = player.getPersistentDataContainer().get(classKey, PersistentDataType.STRING);
        return registry.get(id);
    }

    public boolean hasClass(Player player) {
        return player.getPersistentDataContainer().has(classKey, PersistentDataType.STRING);
    }

    /**
     * Switches a player to the given class: removes their old class's effects
     * (if any), stores the new choice, and applies it.
     */
    public void setClass(Player player, PlayerClass newClass) {
        getClass(player).ifPresent(old -> old.remove(player));

        player.getPersistentDataContainer().set(classKey, PersistentDataType.STRING, newClass.getId());
        newClass.apply(player);
    }

    /**
     * Re-applies a player's stored class. Used on join (in case the server
     * restarted, since attribute modifiers don't persist across restarts)
     * and on respawn (since potion effects are cleared on death).
     */
    public void reapplyStoredClass(Player player) {
        getClass(player).ifPresent(playerClass -> playerClass.apply(player));
    }
}
