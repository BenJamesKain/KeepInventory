package com.github.benjameskain.keepinventory.commands;

import com.github.benjameskain.keepinventory.utilities.DataSaver;
import com.github.benjameskain.keepinventory.utilities.MessageSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class KeepInventory {

    // Datasaver instance
    DataSaver data = DataSaver.getInstance();

    // Create hashmap(UUID, Bool)
    private static final HashMap<UUID, Boolean> playerInventory = new HashMap<>();

    // Permission Strings
    private final String PERMISSION_STRING_DEFAULT = "keepinventory.keepitems";
    private final String PERMISSION_STRING_OTHERS = PERMISSION_STRING_DEFAULT + ".others";


    public KeepInventory() {
        new CommandBase("keepinventory", 0, 2, false) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                // check if sender has permission
                if (!sender.hasPermission(PERMISSION_STRING_DEFAULT)) {
                    MessageSender.sendPermission(sender);
                    return true;
                }

                switch (args.length) {
                    case 0: // keepinventory
                        MessageSender.inventorySaved(sender, isSavingInventory(player));
                    break;

                    case 1: // /keepinventory [true/false]/[player]
                        // change if inventory is being saved
                        if (args[0].equalsIgnoreCase("true") || (args[0].equalsIgnoreCase("false"))) {
                            boolean savingInventory = Boolean.parseBoolean(args[0]);
                            MessageSender.inventoryNowSaved(player, savingInventory);

                            // update player's inventory being kept with the sent options
                            setSavingInventory(player.getUniqueId(), savingInventory);

                        } else if (player.getServer().getPlayer(args[0]) != null) { // /keepinventory [player]
                            // check if sender has permission
                            if (!sender.hasPermission(PERMISSION_STRING_OTHERS)) {
                                MessageSender.sendPermission(sender);
                                break;
                            }

                            Player target = player.getServer().getPlayer(args[0]);
                            assert target != null;

                            MessageSender.inventorySaved(player, target.getName(), isSavingInventory(target));

                        } else {
                            // Invalid usage
                            MessageSender.inventoryUsage(player, 1);
                        }
                        break;

                    case 2: // /keepinventory [player] [true/false]
                        // check if sender has permission
                        if (!sender.hasPermission(PERMISSION_STRING_OTHERS)) {
                            MessageSender.sendPermission(sender);
                            break;
                        }

                        // check if target player is online
                        String targetName = args[0];
                        if (sender.getServer().getPlayer(targetName) == null) {
                            MessageSender.sendNotOnline(sender, targetName);
                            break;
                        }

                        Player target = player.getServer().getPlayer(targetName);
                        assert target != null;

                        // check if sent command is true or false
                        if (!args[1].equalsIgnoreCase("true") && (!args[1].equalsIgnoreCase("false"))) {
                            MessageSender.inventoryUsage(player, 2);
                            break;
                        }

                        boolean savingInventory = Boolean.parseBoolean(args[1]);
                        MessageSender.inventoryNowSaved(player, targetName, savingInventory);

                        // update player's inventory being kept with the sent options
                        setSavingInventory(target.getUniqueId(), savingInventory);
                        break;
                }
                return true;
            }

            @Override
            public String getUsage() {
                return "/keepinventory";
            }
        };
    }

    public boolean isSavingInventory(Player player) {
        UUID uuid = player.getUniqueId();
        boolean savingInventory;

        // Check if player data has been set
        if (data.getData().getConfigurationSection("player." + uuid) == null) {
            // Player data hasn't been set, so start saving now
            setSavingInventory(player.getUniqueId(), true);
        }

        // If player is not stored in the hashmap, set their keepinventory to true
        playerInventory.putIfAbsent(player.getUniqueId(), data.getData().getBoolean("player." + player.getUniqueId() + ".savingInventory"));

        // Get from hashmap
        savingInventory = playerInventory.get(uuid);

        // Changing this line so that we aren't reading from config when we don't need to
        // savingInventory = data.getData().getBoolean("player." + uuid + ".saving");

        return savingInventory;
    }

    public void setSavingInventory(UUID uuid, boolean savingInventory) {
        // Save to hashmap
        playerInventory.put(uuid, savingInventory);
        // write to config
        // In the future, change this so every player has their own file in
        // a folder of playerdata
        data.getData().set("player." + uuid + ".uuid", uuid.toString());
        data.getData().set("player." + uuid + ".savingInventory", savingInventory);
        data.saveData();
    }

    public HashMap<UUID, Boolean> getHashMap() {
        return playerInventory;
    }
}
