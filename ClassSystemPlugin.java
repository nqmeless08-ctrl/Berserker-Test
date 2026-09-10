package com.yourserver.classsystem;

import com.yourserver.classsystem.classes.BerserkerClass;
import com.yourserver.classsystem.classes.ClassManager;
import com.yourserver.classsystem.classes.ClassRegistry;
import com.yourserver.classsystem.commands.ClassCommand;
import com.yourserver.classsystem.commands.SetClassCommand;
import com.yourserver.classsystem.gui.ClassGUIListener;
import com.yourserver.classsystem.listeners.PlayerJoinListener;
import com.yourserver.classsystem.listeners.PlayerRespawnListener;
import com.yourserver.classsystem.listeners.ShieldBlockListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClassSystemPlugin extends JavaPlugin {

    private static ClassSystemPlugin instance;
    private ClassManager classManager;
    private ClassRegistry classRegistry;

    @Override
    public void onEnable() {
        instance = this;

        // Registry of every class type the plugin knows about.
        // Add new classes here as they're built.
        classRegistry = new ClassRegistry();
        classRegistry.register(new BerserkerClass());

        // Handles storing/applying/removing a player's chosen class.
        classManager = new ClassManager(this, classRegistry);

        // Listeners
        getServer().getPluginManager().registerEvents(new ClassGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new ShieldBlockListener(this), this);

        // Commands
        getCommand("class").setExecutor(new ClassCommand(this));
        getCommand("setclass").setExecutor(new SetClassCommand(this));

        getLogger().info("ClassSystem enabled with " + classRegistry.getAll().size() + " class(es) registered.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClassSystem disabled.");
    }

    public static ClassSystemPlugin getInstance() {
        return instance;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public ClassRegistry getClassRegistry() {
        return classRegistry;
    }
}
