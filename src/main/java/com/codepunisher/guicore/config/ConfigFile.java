package com.codepunisher.guicore.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigFile {
    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    protected final FileConfiguration config;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder(), name);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveDefaultConfig() {
        if (!file.exists())
            plugin.saveResource(name, false);
    }

    public void reloadConfig() {
        try {
            config.load(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() { return config; }
}
