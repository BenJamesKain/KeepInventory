package com.github.benjameskain.keepinventory.commands;

import com.github.benjameskain.keepinventory.Main;
import com.github.benjameskain.keepinventory.utilities.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {

    private List<String> delayedPlayers = null;
    private int delay = 0;
    private final int minArgs;
    private final int maxArgs;
    private final boolean playerOnly;

    public CommandBase(String command) {
        this(command, 0);
    }

    public CommandBase(String command, boolean playerOnly) {
        this(command, 0, playerOnly);
    }

    public CommandBase(String command, int requiredArgs) {
        this(command, requiredArgs, requiredArgs);
    }

    public CommandBase(String command, int minArgs, int maxArgs) {
        this(command, minArgs, maxArgs, false);
    }

    public CommandBase(String command, int requiredArgs, boolean playerOnly) {
        this(command, requiredArgs, requiredArgs, playerOnly);
    }

    public CommandBase(String command, int minArgs, int maxArgs, boolean playerOnly) {
        super(command);

        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.playerOnly = playerOnly;

        CommandMap commandMap= getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public CommandBase enableDelay(int delay) {
        this.delay = delay;
        this.delayedPlayers = new ArrayList<>();
        return this;
    }

    public void removeDelay(Player player) {
        this.delayedPlayers.remove(player.getName());
    }

    public void sendUsage(CommandSender sender) {
        MessageSender.send(sender, getUsage());
    }

    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (args.length < minArgs || (args.length > maxArgs && maxArgs != -1)) {
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            MessageSender.send(sender, "&cOnly players can use this command.");
            return true;
        }

        String permission = getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            MessageSender.sendPermission(sender);
            return true;
        }

        if (delayedPlayers != null && sender instanceof Player) {
            Player player = (Player) sender;
            if (delayedPlayers.contains(player.getName())) {
                MessageSender.send(player, "&Please wait before using this command again.");
                return true;
            }

            delayedPlayers.add(player.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                removeDelay(player);
            }, 20L * delay);

        }

        if (!onCommand(sender, args)) {
            sendUsage(sender);
        }

        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        return this.onCommand(sender, args);
    }

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public abstract String getUsage();
}
