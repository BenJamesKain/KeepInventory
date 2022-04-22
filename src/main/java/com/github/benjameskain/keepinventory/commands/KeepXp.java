package com.github.benjameskain.keepinventory.commands;

import com.github.benjameskain.keepinventory.utilities.DataSaver;
import com.github.benjameskain.keepinventory.utilities.MessageSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class KeepXp {

    // Datasaver instance
    DataSaver data = DataSaver.getInstance();

    // Create hashmap(UUID, Bool)
    private static final HashMap<UUID, Boolean> playerXp = new HashMap<>();

    // Permission Strings
    private final String PERMISSION_STRING_XP = "keepinventory.keepxp";
    private final String PERMISSION_STRING_OTHERS = PERMISSION_STRING_XP + ".others";

    public KeepXp() {
        new CommandBase("keepxp", 0, 2, false) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                Player player = (Player) sender;

                // check if sender has permission
                if (!sender.hasPermission(PERMISSION_STRING_XP)) {
                    MessageSender.sendPermission(sender);
                    return true;
                }

                switch (args.length) {
                    case 0: // keepxp
                        MessageSender.xpSaved(sender, isSavingXp(player));
                        break;

                    case 1: // /keepxp [true/false]/[player]
                        // change if xp is being saved
                        if (args[0].equalsIgnoreCase("true") || (args[0].equalsIgnoreCase("false"))) {
                            boolean savingXp = Boolean.parseBoolean(args[0]);
                            MessageSender.xpNowSaved(player, savingXp);

                            // update player's xp being kept with the sent options
                            setSavingXp(player.getUniqueId(), savingXp);

                        } else if (player.getServer().getPlayer(args[0]) != null) { // /keepxp [player]
                            // check if sender has permission
                            if (!sender.hasPermission(PERMISSION_STRING_OTHERS)) {
                                MessageSender.sendPermission(sender);
                                break;
                            }

                            Player target = player.getServer().getPlayer(args[0]);
                            assert target != null;

                            MessageSender.xpSaved(player, target.getName(), isSavingXp(target));

                        } else {
                            // Invalid usage
                            MessageSender.xpUsage(player, 1);
                        }
                        break;

                    case 2: // /keepxp [player] [true/false]
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
                            MessageSender.xpUsage(player, 2);
                            break;
                        }

                        boolean savingXp = Boolean.parseBoolean(args[1]);
                        MessageSender.xpNowSaved(player, targetName, savingXp);

                        // update player's xp being kept with the sent options
                        setSavingXp(target.getUniqueId(), savingXp);
                        break;
                }
                return true;
            }

            @Override
            public String getUsage() {
                return "/keepxp";
            }
        };
    }

    public boolean isSavingXp(Player player) {
        UUID uuid = player.getUniqueId();
        boolean savingXp;

        // Check if player data has been set
        if (data.getData().getConfigurationSection("player." + uuid) == null) {
            // Player data hasn't been set, so start saving now
            setSavingXp(player.getUniqueId(), true);
        }

        // If player is not stored in the hashmap, set their keepxp to true
        playerXp.putIfAbsent(player.getUniqueId(), data.getData().getBoolean("player." + player.getUniqueId() + ".savingXp"));

        // Get from hashmap
        savingXp = playerXp.get(uuid);

        return savingXp;
    }

    public void setSavingXp(UUID uuid, boolean savingXp) {
        // Save to hashmap
        playerXp.put(uuid, savingXp);
        // write to config
        // In the future, change this so every player has their own file in
        // a folder of playerdata
        data.getData().set("player." + uuid + ".uuid", uuid.toString());
        data.getData().set("player." + uuid + ".savingXp", savingXp);
        data.saveData();
    }

    public HashMap<UUID, Boolean> getHashMap() {
        return playerXp;
    }
}
