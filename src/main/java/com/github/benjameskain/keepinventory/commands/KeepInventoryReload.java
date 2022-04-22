package com.github.benjameskain.keepinventory.commands;

import com.github.benjameskain.keepinventory.utilities.DataSaver;
import com.github.benjameskain.keepinventory.utilities.MessageSender;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class KeepInventoryReload {

    DataSaver data = DataSaver.getInstance();
    KeepInventory keepInventory = new KeepInventory();
    KeepXp keepXp = new KeepXp();

    private final String PERMISSION_RELOAD_STRING = "keepinventory.reload";

    public KeepInventoryReload() {
        new CommandBase("keepinventoryreload", 0, 0, false) {

            @Override
            public boolean onCommand(CommandSender sender, String[] args) {

                if (!sender.hasPermission(PERMISSION_RELOAD_STRING)) {
                    MessageSender.sendPermission(sender);
                    return true;
                }

                    data.reloadConfig();
                    data.reloadData();

                    onReload();

                    MessageSender.send(sender, "&aPlugin Reloaded!");
                    return true;
            }

            @Override
            public String getUsage() {
                return "/keepinventoryreload";
            }
        };
    }

    public void onReload() {
        // When config is reloaded, store the data saved in data.yml
        // to the hashmap. This prevents the plugin from breaking if
        // the data.yml file is changed & reloaded while the server is running.

        // Check if there is any playerdata saved to the config
        if (data.getData().getConfigurationSection("player") == null) return;
        if (data.getData().getConfigurationSection("player").getKeys(false).isEmpty()) return;

        // For each entry in the player: section
        for (String key : data.getData().getConfigurationSection("player").getKeys(false)) {

            // Read the data from the file
            String uuidString = data.getData().getString("player." + key + ".uuid");
            UUID uuid = UUID.fromString(uuidString);
            boolean savingInventory = data.getData().getBoolean("player." + key + ".savingInventory");
            boolean savingXp = data.getData().getBoolean("player." + key + ".savingXp");

            // Update the hashmaps accordingly
            keepInventory.getHashMap().put(uuid, savingInventory);
            //System.out.println(uuidString + ": " + savingInventory + " saved to keepInventory HashMap.");
            keepXp.getHashMap().put(uuid, savingXp);
            //System.out.println(uuidString + ": " + savingXp + " saved to keepXp HashMap.");

        }
    }
}
