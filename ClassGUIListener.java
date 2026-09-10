package com.yourserver.classsystem.gui;

import com.yourserver.classsystem.ClassSystemPlugin;
import com.yourserver.classsystem.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class ClassGUIListener implements Listener {

    private final ClassSystemPlugin plugin;

    public ClassGUIListener(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title() == null) return;
        if (!event.getView().title().equals(Component.text(ClassGUI.TITLE))) return;

        event.setCancelled(true); // never let players take the icons

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        String classId = meta.getPersistentDataContainer().get(ClassGUI.CLASS_ITEM_KEY, PersistentDataType.STRING);
        if (classId == null) return;

        Optional<PlayerClass> playerClass = plugin.getClassRegistry().get(classId);
        if (playerClass.isEmpty()) return;

        plugin.getClassManager().setClass(player, playerClass.get());
        player.closeInventory();
        player.sendMessage(Component.text("You are now a " + playerClass.get().getDisplayName() + "!", NamedTextColor.GREEN));
    }
}
