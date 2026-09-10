package com.yourserver.classsystem.listeners;

import com.yourserver.classsystem.ClassSystemPlugin;
import com.yourserver.classsystem.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Cancels the shield-raising interaction for players whose class forbids
 * shield use (e.g. Berserker). This stops the "block with shield" action
 * outright rather than letting them raise it and reducing its effect,
 * matching "cannot use a shield" as a hard restriction.
 */
public class ShieldBlockListener implements Listener {

    private final ClassSystemPlugin plugin;

    // Simple per-player cooldown so spamming right-click doesn't spam chat.
    private final Map<UUID, Long> lastWarned = new HashMap<>();
    private static final long WARN_COOLDOWN_MS = 2000;

    public ShieldBlockListener(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.SHIELD) return;
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        Optional<PlayerClass> playerClass = plugin.getClassManager().getClass(player);
        if (playerClass.isEmpty() || !playerClass.get().blocksShieldUse()) return;

        event.setCancelled(true);

        long now = System.currentTimeMillis();
        long last = lastWarned.getOrDefault(player.getUniqueId(), 0L);
        if (now - last > WARN_COOLDOWN_MS) {
            lastWarned.put(player.getUniqueId(), now);
            player.sendMessage(Component.text(
                    playerClass.get().getDisplayName() + "s can't use shields!", NamedTextColor.RED));
        }
    }
}
