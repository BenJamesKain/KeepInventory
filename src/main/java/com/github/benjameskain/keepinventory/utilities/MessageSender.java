package com.github.benjameskain.keepinventory.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageSender {

    // Inventory Message Strings
    private final static String INV_STRING = "inventory";
    private final static String XP_STRING = "experience";

    private final static String INVENTORY_USAGE_STRING = "/keepinventory";
    private final static String XP_USAGE_STRING = "/keepxp";
    private final static String HELP_USAGE_STRING = "/keepinventoryhelp";

    private final static String SAVED = "is being saved.";
    private final static String NOT_SAVED = "is NOT being saved.";

    private final static String NOW_SAVED = "is now being saved.";
    private final static String NO_LONGER_SAVED = "is no longer being saved.";


    // Send Methods
    public static void send(CommandSender sender, String message) {
        send(sender, message, "&c");
    }

    public static void send(CommandSender sender, String message, String prefix) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void sendPermission(CommandSender sender) {
        send(sender, "&cYou do not have permission to use this command.");
    }

    public static void sendNotOnline(CommandSender sender, String targetName) {
        send(sender, "&c" + targetName + " is not online!");
    }

    // Helper string method
    private static void sendUsage(CommandSender sender, int args, String usageString) {
        switch(args) {
            case 0:
                send(sender, "&cUsage: &6/" + usageString);
                break;
            case 1:
                send(sender, "&cUsage: &6/" + usageString + " [true/false]");
                break;
            case 2:
                send(sender, "&cUsage: &6/" + usageString + " [player] [true/false]");
                break;
        }
    }

    // Called methods
    public static void inventoryUsage(CommandSender sender, int args) {
        sendUsage(sender, args, INVENTORY_USAGE_STRING);
    }

    public static void xpUsage(CommandSender sender, int args) {
        sendUsage(sender, args, XP_USAGE_STRING);
    }

    public static void helpUsage(CommandSender sender) { sendUsage(sender, 0, HELP_USAGE_STRING);}


    // Helper string methods
    private static void sendSavedMessage(CommandSender sender, String target, boolean isSaved, String itemToSave, String key) {
        if (target == null) { // no target
            if (isSaved) {
                send(sender, "&aYour " + itemToSave + " " + key);
            } else {
                send(sender, "&cYour " + itemToSave + " " + key);
            }
        } else {
            if (isSaved) {
                send(sender, "&a" + target + "'s " + itemToSave + " " + key);
            } else {
                send(sender, "&c" + target + "'s " + itemToSave + " " + key);
            }
        }
    }

    private static void saved(CommandSender sender, String target, boolean isSaved, String itemToSave) {
        if (isSaved) {
            sendSavedMessage(sender, target, true, itemToSave, SAVED);
        } else {
            sendSavedMessage(sender, target, false, itemToSave, NOT_SAVED);
        }
    }

    private static void nowSaved(CommandSender sender, String target, boolean isSaved, String itemToSave) {
        if (isSaved) {
            sendSavedMessage(sender, target, true, itemToSave, NOW_SAVED);
        } else {
            sendSavedMessage(sender, target, false, itemToSave, NO_LONGER_SAVED);
        }
    }


    // Called methods
    // Saved
    public static void inventorySaved(CommandSender sender, boolean isSaved) {
        saved(sender, null, isSaved, INV_STRING);
    }

    public static void inventorySaved(CommandSender sender, String target, boolean isSaved) {
        saved(sender, target, isSaved, INV_STRING);
    }

    public static void xpSaved(CommandSender sender, boolean isSaved) {
        saved(sender, null, isSaved, XP_STRING);
    }

    public static void xpSaved(CommandSender sender, String target, boolean isSaved) {
        saved(sender, target, isSaved, XP_STRING);
    }

    // Now Saved
    public static void inventoryNowSaved(CommandSender sender, boolean isSaved) {
        nowSaved(sender, null, isSaved, INV_STRING);
    }

    public static void inventoryNowSaved(CommandSender sender, String target, boolean isSaved) {
        nowSaved(sender, target, isSaved, INV_STRING);
    }

    public static void xpNowSaved(CommandSender sender, boolean isSaved) {
        nowSaved(sender, null, isSaved, XP_STRING);
    }

    public static void xpNowSaved(CommandSender sender, String target, boolean isSaved) {
        nowSaved(sender, target, isSaved, XP_STRING);
    }

}
