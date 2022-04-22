package com.github.benjameskain.keepinventory.utilities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryHandler {

    private static InventoryHandler instance = new InventoryHandler();
    private HashMap<Player, ItemStack[]> inventory;
    private HashMap<Player, ItemStack[]> armor;

    private InventoryHandler() {
        inventory = new HashMap<Player, ItemStack[]>();
        armor = new HashMap<Player, ItemStack[]>();
    }

    public static InventoryHandler getInstance() {
        return instance;
    }

    public void saveInventory(Player player) {
        ItemStack[] tempInventory = new ItemStack[player.getInventory().getSize()];
        ItemStack[] tempArmor = new ItemStack[player.getInventory().getArmorContents().length];
        tempInventory = player.getInventory().getContents();
        tempArmor = player.getInventory().getArmorContents();
        inventory.put(player, tempInventory);
        armor.put(player, tempArmor);
    }

    public ItemStack[] getInventory(Player player) {
        return (ItemStack[]) inventory.get(player);
    }

    public ItemStack[] getArmor(Player player) {
        return (ItemStack[]) armor.get(player);
    }

    public void removeInventory(Player player) {
        inventory.remove(player);
        armor.remove(player);
    }

    public boolean hasInventorySaved(Player player) {
        return inventory.containsKey(player);
    }

    public boolean hasArmorSaved(Player player) {
        return armor.containsKey(player);
    }
}
