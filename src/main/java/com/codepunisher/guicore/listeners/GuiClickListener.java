package com.codepunisher.guicore.listeners;

import com.codepunisher.guicore.events.CustomGuiClickEvent;
import com.codepunisher.guicore.events.CustomGuiOpenEvent;
import com.codepunisher.guicore.models.CustomClick;
import com.codepunisher.guicore.models.Gui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiClickListener implements Listener {
    private final Map<UUID, Gui> guiPlayers = new HashMap<>();

    // Helps determine if player is in custom menu
    @EventHandler
    public void onCustomGuiOpen(CustomGuiOpenEvent event) {
        guiPlayers.put(event.getPlayer().getUniqueId(), event.getGui());
    }

    // Calls custom click event
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();

        if (item != null && inventory != null) {
            Player player = (Player) event.getWhoClicked();

            Gui gui = guiPlayers.get(player.getUniqueId());

            if (gui == null)
                return;

            event.setCancelled(true);

            // Making sure player is clicking in top menu (before making checks)
            if (Objects.equals(player.getInventory(), inventory))
                return;

            int slot = event.getSlot();
            CustomClick customClick = gui.getCustomClickFromIndex(slot);

            if (customClick == null)
                return;

            CustomGuiClickEvent guiClickEvent = new CustomGuiClickEvent(player, slot, customClick);
            Bukkit.getPluginManager().callEvent(guiClickEvent);

            if (guiClickEvent.isCancelled())
                return;

            customClick.handleClick(player);
        }
    }

    // Removing player from map
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        guiPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        guiPlayers.remove(event.getPlayer().getUniqueId());
    }
}
