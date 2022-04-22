package com.github.benjameskain.keepinventory.listeners;

import com.github.benjameskain.keepinventory.commands.KeepInventory;
import com.github.benjameskain.keepinventory.commands.KeepXp;
import com.github.benjameskain.keepinventory.utilities.InventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.lang.reflect.Method;

public class KeepInventoryListener implements Listener {

    KeepInventory keepInventory = new KeepInventory();
    KeepXp keepXp = new KeepXp();

    private final boolean hasMethod = hasMethod("setKeepInventory");

    private final String PERMISSION_DEFAULT_STRING = "keepinventory.keepitems";
    private final String PERMISSION_XP_STRING = "keepinventory.keepxp";

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();

        // Check if player has the permission and their inventory is being saved.
        // This prevents the user from dying and having their items taken away
        // (when InventoryHandler is saving), and never given back.
        // This can happen when the permission is taken away for keeping items.
        if (player.hasPermission(PERMISSION_DEFAULT_STRING) && (keepInventory.isSavingInventory(player))) {
            if (hasMethod) {

            }
            InventoryHandler.getInstance().saveInventory(player);
            event.getDrops().clear();
        }

        if (player.hasPermission(PERMISSION_XP_STRING) && (keepXp.isSavingXp(player))) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }

    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasPermission(PERMISSION_DEFAULT_STRING) && (keepInventory.isSavingInventory(player))) {
            InventoryHandler handler = InventoryHandler.getInstance();

            if ((handler.hasInventorySaved(player)) && (handler.hasArmorSaved(player))) {
                player.getInventory().setContents(handler.getInventory(player));
                player.getInventory().setArmorContents(handler.getArmor(player));
                handler.removeInventory(player);
            }
        }
    }


    // Check if the player has the method of "setKeepInventory"
    // This ensures interference with other plugins doesn't happen
    private boolean hasMethod(String string) {
        boolean hasMethod = false;
        Method[] methods = PlayerDeathEvent.class.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(string)) {
                hasMethod = true;
                break;
            }
        }
        return hasMethod;
    }
}
