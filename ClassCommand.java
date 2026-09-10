package com.yourserver.classsystem.commands;

import com.yourserver.classsystem.ClassSystemPlugin;
import com.yourserver.classsystem.classes.PlayerClass;
import com.yourserver.classsystem.gui.BedrockClassForm;
import com.yourserver.classsystem.gui.ClassGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class ClassCommand implements CommandExecutor {

    private final ClassSystemPlugin plugin;

    public ClassCommand(ClassSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        // Bedrock players get a native form; Java players get the inventory GUI.
        // isBedrockPlayer() safely returns false if Floodgate isn't installed at all.
        if (isFloodgateInstalled() && BedrockClassForm.isBedrockPlayer(player)) {
            BedrockClassForm.send(player, new ArrayList<>(plugin.getClassRegistry().getAll()));
        } else {
            ClassGUI.open(player, plugin.getClassRegistry().getAll());
        }

        return true;
    }

    private boolean isFloodgateInstalled() {
        Plugin floodgate = plugin.getServer().getPluginManager().getPlugin("floodgate");
        return floodgate != null && floodgate.isEnabled();
    }
}
