package com.yourserver.classsystem.listeners;

import com.yourserver.classsystem.ClassSystemPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final ClassSystemPlugin plugin;

    public PlayerRespawnListener(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Potion effects (like Berserker's permanent Speed I) are cleared on death.
        // Attribute modifiers survive death, so reapplying is safe/idempotent either way
        // since BerserkerClass removes-then-reapplies its own modifiers by key.
        Bukkit.getScheduler().runTask(plugin, () -> plugin.getClassManager().reapplyStoredClass(player));
    }
}
