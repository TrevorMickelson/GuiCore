package com.codepunisher.guicore.models;

import com.codepunisher.guicore.GuiCorePlugin;
import com.mcaim.core.util.Util;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Gui {
    private final String title;
    private final int size;
    private final Map<Integer, ItemStack> items;
    private final Map<Integer, CustomClick> customClicks;
    private final String permission;
    private final ItemStack noPermissionItem;
    private final ItemStack backGroundItem;

    private Gui(GuiBuilder guiBuilder) {
        title = guiBuilder.title;
        size = guiBuilder.size;
        items = guiBuilder.items;
        customClicks = guiBuilder.customClicks;
        permission = guiBuilder.permission;
        noPermissionItem = guiBuilder.noPermissionItem;
        backGroundItem = guiBuilder.backGroundItem;
    }

    public CustomClick getCustomClickFromIndex(int index) { return customClicks.get(index); }
    public String getPermission() { return permission; }

    /**
     * Creating an open method rather than
     * using an inventory object for certain
     * customization features. Sometimes the same
     * slot might have multiple items, or a placeholder
     * from placeholderapi
     */
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < size; i++) {
            ItemStack itemAtIndex = items.get(i);

            // Placing item if it exists
            if (itemAtIndex != null) {
                if (GuiCorePlugin.getInstance().isPlaceHolderAPI()) {
                    handlePlaceHolderApi(player, itemAtIndex);
                }

                // Checking if there's a permission
                // requirement for the item stack
                CustomClick customClick = customClicks.get(i);

                if (customClick != null) {
                    if (!customClick.hasPermission(player)) {
                        inventory.setItem(i, noPermissionItem);
                        continue;
                    }
                }

                inventory.setItem(i, itemAtIndex);
                continue;
            }

            // Background item (if needed)
            if (backGroundItem != null)
                inventory.setItem(i, backGroundItem);
        }

        player.openInventory(inventory);
    }

    /**
     * Converts itemstack lore to work
     * with placeholder api
     */
    private void handlePlaceHolderApi(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

        // Name
        meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));

        // Lore
        List<String> listLore = meta.getLore();

        if (listLore == null || listLore.isEmpty()) return;

        List<String> newLoreList = new LinkedList<>();

        listLore.forEach(lore -> {
            String newLore = PlaceholderAPI.setPlaceholders(player, lore);
            newLoreList.add(newLore);
        });

        meta.setLore(newLoreList);
        item.setItemMeta(meta);
    }

    public static class GuiBuilder {
        private final String title;
        private final int size;
        private final Map<Integer, ItemStack> items = new HashMap<>();
        private final Map<Integer, CustomClick> customClicks = new HashMap<>();
        private String permission;
        private ItemStack noPermissionItem;
        private ItemStack backGroundItem;

        public GuiBuilder(String title, int size) {
            this.title = Util.trans(title);
            this.size = size;
        }

        public void addNormalItem(int index, ItemStack itemStack) {
            items.put(index, itemStack);
        }

        public void addCustomClick(int index, CustomClick customClick) {
            customClicks.put(index, customClick);
        }

        public void permission(String permission) {
            this.permission = permission;
        }

        public void noPermissionItem(ItemStack noPermissionItem) {
            this.noPermissionItem = noPermissionItem;
        }

        public void backGroundItem(ItemStack backGroundItem) {
            this.backGroundItem = backGroundItem;
        }

        public Gui build() {
            return new Gui(this);
        }
    }
}
