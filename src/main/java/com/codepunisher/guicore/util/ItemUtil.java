package com.codepunisher.guicore.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class ItemUtil {
    public static boolean hasUniqueKey(ItemStack item, String key) {
        String uniqueKey = getUniqueKey(item);
        return uniqueKey != null && uniqueKey.equalsIgnoreCase(key);
    }

    public static String getUniqueKey(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null)
            return null;

        PersistentDataContainer data = meta.getPersistentDataContainer();

        for (NamespacedKey key : data.getKeys())
            return key.getKey();

        return null;
    }
}