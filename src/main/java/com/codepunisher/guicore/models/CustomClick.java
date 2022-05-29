package com.codepunisher.guicore.models;

import com.codepunisher.guicore.GuiCorePlugin;
import com.codepunisher.guicore.util.ItemUtil;
import com.codepunisher.guicore.util.Pair;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public final class CustomClick {
    private final List<String> playerCommands;
    private final List<String> consoleCommands;
    private final String permission;
    private final String message;
    private final String sound;
    private final boolean closeOnClick;
    private final CustomCost customCost;

    private CustomClick(CustomClickBuilder buttonBuilder) {
        playerCommands = buttonBuilder.playerCommands;
        consoleCommands = buttonBuilder.consoleCommands;
        permission = buttonBuilder.permission;
        message = buttonBuilder.message;
        sound = buttonBuilder.sound;
        closeOnClick = buttonBuilder.closeOnClick;
        customCost = buttonBuilder.customCost;
    }

    /**
     * Permission is specifically designed
     * to display a no permission panel
     */
    public boolean hasPermission(Player player) {
        return permission == null || permission.isEmpty() || player.hasPermission(permission);
    }

    public void handleClick(Player player) {
        if (!hasPermission(player))
            return;

        if (!canAffordCost(player)) {
            player.sendMessage(ChatColor.RED + "Sorry, but you can't afford this!");
            return;
        }

        if (playerCommands != null)
            playerCommands.forEach(player::performCommand);

        if (consoleCommands != null) {
            consoleCommands.forEach(cmd -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
            });
        }

        if (message != null)
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        if (sound != null)
            player.playSound(player.getLocation(), Sound.valueOf(sound.toUpperCase()), 1.0f, 1.0f);

        if (closeOnClick)
            player.closeInventory();
    }

    private boolean canAffordCost(Player player) {
        Economy economy = GuiCorePlugin.getInstance().getEconomy();

        if (economy != null) {
            double currency = economy.getBalance(player);
            double cost = customCost.getVaultCost();

            if (currency < cost)
                return false;
        }

        if (!customCost.getKeyCost().isEmpty()) {
            List<Pair<String, Integer>> itemKeys = customCost.getKeyCost();
            boolean hasEnoughItems = true;

            for (Pair<String, Integer> pair : itemKeys) {
                if (!hasEnoughCustomItems(player, pair.getKey(), pair.getValue())) {
                    hasEnoughItems = false;
                }
            }

            if (hasEnoughItems)
                removeItemKeyItemsFromInventory(player);

            return hasEnoughItems;
        }

        return true;
    }

    private void removeItemKeyItemsFromInventory(Player player) {
        List<Pair<String, Integer>> itemKeys = customCost.getKeyCost();

        for (Pair<String, Integer> pair : itemKeys) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null)
                    continue;

                if (!ItemUtil.hasUniqueKey(item, pair.getKey()))
                    continue;

                item.setAmount(item.getAmount() - pair.getValue());
            }
        }
    }

    private boolean hasEnoughCustomItems(Player player, String key, int value) {
        int itemCounter = 0;

        // Counting all gems in the players inventory
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null)
                continue;

            if (!ItemUtil.hasUniqueKey(item, key))
                continue;

            itemCounter += item.getAmount();
        }

        // Making sure the gem count is
        // greater or equal to the gem cost
        return itemCounter >= value;
    }

    public static CustomClickBuilder create() {
        return new CustomClickBuilder();
    }

    public static class CustomClickBuilder {
        private List<String> playerCommands = new LinkedList<>();
        private List<String> consoleCommands = new LinkedList<>();
        private String sound = null;
        private String permission = null;
        private String message = null;
        private boolean closeOnClick;
        private CustomCost customCost;

        public CustomClickBuilder playerCommands(List<String> commands) {
            this.playerCommands = commands;
            return this;
        }

        public CustomClickBuilder consoleCommands(List<String> commands) {
            this.consoleCommands = commands;
            return this;
        }

        public CustomClickBuilder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public CustomClickBuilder message(String message) {
            this.message = message;
            return this;
        }

        public CustomClickBuilder sound(String sound) {
            this.sound = sound;
            return this;
        }

        public CustomClickBuilder closeOnClick(boolean closeOnClick) {
            this.closeOnClick = closeOnClick;
            return this;
        }

        public CustomClickBuilder customCost(CustomCost customCost) {
            this.customCost = customCost;
            return this;
        }

        public CustomClick build() {
            return new CustomClick(this);
        }
    }
}
