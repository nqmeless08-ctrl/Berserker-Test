package com.yourserver.classsystem.listeners;

import com.yourserver.classsystem.ClassSystemPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ClassSystemPlugin plugin;

    public PlayerJoinListener(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Attribute modifiers are per-session in some setups and can be lost across
        // restarts; potion effects definitely don't persist. Reapply on every join
        // so a returning player's class effects are always correct.
        plugin.getClassManager().reapplyStoredClass(player);
    }
}
