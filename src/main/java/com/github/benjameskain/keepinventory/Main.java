package com.github.benjameskain.keepinventory;

import com.github.benjameskain.keepinventory.commands.KeepInventory;
import com.github.benjameskain.keepinventory.commands.KeepInventoryHelp;
import com.github.benjameskain.keepinventory.commands.KeepInventoryReload;
import com.github.benjameskain.keepinventory.commands.KeepXp;
import com.github.benjameskain.keepinventory.listeners.KeepInventoryListener;
import com.github.benjameskain.keepinventory.utilities.DataSaver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    private static Main instance;
    DataSaver data = DataSaver.getInstance();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        data.setup(this);

        Bukkit.getPluginManager().registerEvents(new KeepInventoryListener(), this);
        new KeepInventory();
        new KeepXp();
        new KeepInventoryReload();
        new KeepInventoryHelp();
    }

    @Override
    public void onDisable() {}

    public static Main getInstance() {
        return instance;
    }
}
