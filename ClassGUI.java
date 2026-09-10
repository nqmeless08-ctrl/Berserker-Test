package com.yourserver.classsystem.gui;

import com.yourserver.classsystem.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Builds (but doesn't listen for clicks on — see ClassGUIListener) the
 * class-selection inventory shown to Java players.
 */
public class ClassGUI {

    public static final String TITLE = "Choose Your Class";
    public static final NamespacedKey CLASS_ITEM_KEY = new NamespacedKey("classsystem", "gui_class_id");

    public static Inventory build(Collection<PlayerClass> classes) {
        int size = Math.max(9, ((classes.size() / 9) + 1) * 9);
        Inventory inv = Bukkit.createInventory(null, size, Component.text(TITLE));

        for (PlayerClass playerClass : classes) {
            inv.addItem(buildIcon(playerClass));
        }

        return inv;
    }

    private static ItemStack buildIcon(PlayerClass playerClass) {
        ItemStack item = new ItemStack(playerClass.getIcon());
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(playerClass.getDisplayName(), NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(playerClass.getDescription(), NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        for (String stat : playerClass.getStatLines()) {
            lore.add(Component.text(stat, NamedTextColor.YELLOW)
                    .decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());
        lore.add(Component.text("Click to select", NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(lore);
        meta.getPersistentDataContainer().set(CLASS_ITEM_KEY, PersistentDataType.STRING, playerClass.getId());
        item.setItemMeta(meta);
        return item;
    }

    public static void open(Player player, Collection<PlayerClass> classes) {
        player.openInventory(build(classes));
    }
}
