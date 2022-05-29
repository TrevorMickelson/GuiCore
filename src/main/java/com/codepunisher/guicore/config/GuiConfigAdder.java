package com.codepunisher.guicore.config;

import com.codepunisher.guicore.GuiCorePlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class GuiConfigAdder extends GuiConfig {
    private final ItemStack[] itemStacks;
    private byte pathIndex = 0;

    private GuiConfigAdder(ItemStack[] itemStacks) {
        super(UUID.randomUUID() + ".yml");
        this.itemStacks = itemStacks;
    }

    public static void addItemsToNewFile(ItemStack[] itemStacks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new GuiConfigAdder(itemStacks).addNewGuiFile();
            }
        }.runTaskAsynchronously(GuiCorePlugin.getInstance());
    }

    private void addNewGuiFile() {
        addMainGuiInfo();

        for (byte i = 0; i < itemStacks.length; i++) {
            ItemStack item = itemStacks[i];

            if (item == null || item.getType() == Material.AIR)
                continue;

            String pathIndex = String.valueOf(this.pathIndex);
            String path = getItemStackPathAtTarget(pathIndex);
            addItemStackToFile(item, path);

            // Setting slot
            config.set(getPathAtTarget(pathIndex) + "Slot", i);
            this.pathIndex++;
        }

        saveConfig();
    }

    private void addMainGuiInfo() {
        config.set("Command", "");
        config.set("Permission", "");
        config.set("Title", "");
        config.set("Size", 54);
        addItemStackToFile(new ItemStack(Material.RED_STAINED_GLASS_PANE), "NoPermissionItem.");
        addItemStackToFile(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "BackGroundItem.");
    }

    private void addItemStackToFile(ItemStack item, String path) {
        GuiCorePlugin corePlugin = GuiCorePlugin.getInstance();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String name = meta.getDisplayName();

            if (!name.isEmpty())
                config.set(path + "Name", name);

            List<String> lore = meta.getLore();

            if (lore != null && !lore.isEmpty())
                config.set(path + "Lore", lore);

            List<String> flags = new LinkedList<>();
            meta.getItemFlags().forEach((flag -> { flags.add(flag.name().toUpperCase()); }));

            if (!flags.isEmpty())
                config.set(path + "Flags", flags);

            List<String> enchants = new LinkedList<>();
            meta.getEnchants().forEach((k, v) -> {
                enchants.add(k.getName().toUpperCase() + ":" + v);
            });

            if (!enchants.isEmpty())
                config.set(path + "Enchants", enchants);
        }

        config.set(path + "Material", item.getType().name());

        // Head database support
        if (corePlugin.isHeadDataBaseApiEnabled()) {
            String id = corePlugin.getHeadDatabaseApi().getItemID(item);

            if (id != null) {
                config.set(path + "HeadValue", id);
            }
        }

        config.set(path + "Amount", item.getAmount());
    }
}
