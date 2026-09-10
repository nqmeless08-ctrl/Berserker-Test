package com.yourserver.classsystem.commands;

import com.yourserver.classsystem.ClassSystemPlugin;
import com.yourserver.classsystem.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SetClassCommand implements CommandExecutor {

    private final ClassSystemPlugin plugin;

    public SetClassCommand(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /setclass <player> <classid>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        Optional<PlayerClass> playerClass = plugin.getClassRegistry().get(args[1]);
        if (playerClass.isEmpty()) {
            sender.sendMessage(Component.text("Unknown class id: " + args[1], NamedTextColor.RED));
            return true;
        }

        plugin.getClassManager().setClass(target, playerClass.get());
        sender.sendMessage(Component.text(
                "Set " + target.getName() + "'s class to " + playerClass.get().getDisplayName() + ".",
                NamedTextColor.GREEN));
        target.sendMessage(Component.text(
                "Your class has been set to " + playerClass.get().getDisplayName() + ".",
                NamedTextColor.GREEN));

        return true;
    }
}
