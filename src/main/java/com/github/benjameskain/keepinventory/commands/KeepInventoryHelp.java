package com.github.benjameskain.keepinventory.commands;

import com.github.benjameskain.keepinventory.utilities.MessageSender;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class KeepInventoryHelp {

    // Permission Strings
    private final String PERMISSION_HELP_STRING = "keepinventory.help";
    private final String PERMISSION_KEEPINVENTORY_STRING = "keepinventory.keepitems";
    private final String PERMISSION_KEEPXP_STRING = "keepinventory.keepxp";
    private final String PERMISSION_KEEPINVENTORY_OTHERS_STRING = "keepinventory.keepitems.others";
    private final String PERMISSION_KEEPXP_OTHERS_STRING = "keepinventory.keepxp.others";
    private final String PERMISSION_KEEPINVENTORY_RELOAD_STRING = "keepinventory.reload";

    // Message strings
    private final String PCOLOR = "&3";
    private final String SCOLOR = "&8";
    private final String HCOLOR = "&b";

    private final String TITLE = SCOLOR + "&m---" + HCOLOR + " &lKeepInventory Help" + SCOLOR + " &m ---";
    private final String PREFIX = SCOLOR + "-/" + PCOLOR;
    private final String SEPERATOR = SCOLOR + "- ";
    private final String TARGET = HCOLOR + "[target]";
    private final String TRUE_FALSE = HCOLOR + "[true | false]";

    private final String INVENTORY_STRING = "inventory";
    private final String XP_STRING = "experience";

    private final String INVENTORY_CMD = "keepinventory";
    private final String XP_CMD = "keepxp";
    private final String HELP_CMD =  "keepinventoryhelp";
    private final String RELOAD_CMD = "keepinventoryreload";

    public KeepInventoryHelp() {
        new CommandBase("keepinventoryhelp", 0, true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] args) {
                if (!sender.hasPermission(PERMISSION_HELP_STRING)) {
                    MessageSender.sendPermission(sender);
                    return true;
                }

                if (args.length > 0) {
                    MessageSender.helpUsage(sender);
                }

                MessageSender.send(sender, TITLE);
                if (sender.hasPermission(PERMISSION_KEEPINVENTORY_STRING)) {
                    helpMessager(sender, INVENTORY_CMD);
                    helpMessagerArgs(sender, INVENTORY_CMD);
                }

                if (sender.hasPermission(PERMISSION_KEEPINVENTORY_OTHERS_STRING)) {
                    helpMessagerPlayer(sender, INVENTORY_CMD);
                    helpMessagerPlayerArgs(sender, INVENTORY_CMD);
                }

                if (sender.hasPermission(PERMISSION_KEEPXP_STRING)) {
                    helpMessager(sender, XP_CMD);
                    helpMessagerArgs(sender, XP_CMD);
                }

                if (sender.hasPermission(PERMISSION_KEEPXP_OTHERS_STRING)) {
                    helpMessagerPlayer(sender, XP_CMD);
                    helpMessagerPlayerArgs(sender, XP_CMD);
                }

                if (sender.hasPermission(PERMISSION_KEEPINVENTORY_RELOAD_STRING)) {
                    helpMessager(sender, RELOAD_CMD, "reload the plugin.");
                }
                helpMessager(sender, HELP_CMD, "view this message.");
                return true;
            }

            @Override
            public String getUsage() {
                return "/keepinventoryhelp";
            }
        };
    }

    private String descBuilder(String command, boolean player, boolean args) {
        String descString;

        if (!args) { // not setting true/false
            descString = "see if ";
        } else {
            descString = "change if ";
        }

        if (!player) { // setting for yourself
            descString += "your ";
        } else {
            descString += "another player's ";
        }

        if (Objects.equals(command, INVENTORY_CMD)) {
            descString += INVENTORY_STRING;

        } else if (Objects.equals(command, XP_CMD)) {
            descString += XP_STRING;
        }

        descString += " is being saved.";

        return descString;
    }

    private void helpMessager(CommandSender sender, String command) {
        helpMessager(sender, command, false, false);
    }

    private void helpMessagerPlayer(CommandSender sender, String command) {
        helpMessager(sender, command, true, false);
    }

    private void helpMessagerArgs(CommandSender sender, String command) {
        helpMessager(sender, command, false, true);
    }

    private void helpMessagerPlayerArgs(CommandSender sender, String command) {
        helpMessager(sender, command, true, true);
    }

    private void helpMessager(CommandSender sender, String command, boolean player, boolean args) {
        String builderString = " ";
        if (player) {
            builderString += TARGET + SCOLOR + " ";
        }

        if (args) {
            builderString += TRUE_FALSE + SCOLOR + " ";
        }

        String desc = descBuilder(command, player, args);

        MessageSender.send(sender, PREFIX + command + builderString + SEPERATOR + desc);
    }

    private void helpMessager(CommandSender sender, String command, String desc) {
        MessageSender.send(sender, PREFIX + command + " " + SEPERATOR + desc);
    }
}
