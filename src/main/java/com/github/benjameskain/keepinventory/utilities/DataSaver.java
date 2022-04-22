package com.github.benjameskain.keepinventory.utilities;


import com.github.benjameskain.keepinventory.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataSaver {

    FileConfiguration config;
    File configFile;

    FileConfiguration data;
    File dataFile;

    private DataSaver() {}

    static DataSaver instance = new DataSaver();

    public static DataSaver getInstance() {
        return instance;
    }

    public void setup(Main plugin) {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
        saveConfig();

        if (!plugin.getDataFolder().exists()) {
            try {
                plugin.getDataFolder().createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create file!");
            }
        }

        dataFile = new File(plugin.getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
            }
        }

        data = YamlConfiguration.loadConfiguration(dataFile);


    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }

    public void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(dataFile);
    }
}
