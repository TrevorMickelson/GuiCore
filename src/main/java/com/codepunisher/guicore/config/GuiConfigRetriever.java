package com.codepunisher.guicore.config;

import com.codepunisher.guicore.GuiCorePlugin;
import com.codepunisher.guicore.models.CustomClick;
import com.codepunisher.guicore.models.CustomCost;
import com.codepunisher.guicore.models.Gui;
import com.codepunisher.guicore.util.Pair;
import com.codepunisher.guicore.util.Util;
import me.lucko.helper.item.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class GuiConfigRetriever extends GuiConfig {
    GuiConfigRetriever(String name) {
        super(name);
    }

    public Gui retrieveGuiFromFile() {
        Gui.GuiBuilder guiBuilder = getGuiBuilderFromFile();

        // Permission
        String permission = config.getString("Permission");
        guiBuilder.permission(permission);

        // No permission item
        if (config.isSet("NoPermissionItem")) {
            ItemStack noPerm = retrieveItemStackAtPath("NoPermissionItem.");
            guiBuilder.noPermissionItem(noPerm);
        }

        if (config.isSet("BackGroundItem")) {
            // Background item
            ItemStack backGroundItem = retrieveItemStackAtPath("BackGroundItem.");
            guiBuilder.backGroundItem(backGroundItem);
        }

        for (String value : getConfigurationSectionValues()) {
            int slot = config.getInt(getPathAtTarget(value) + "Slot");

            // ItemStack
            String itemStackPath = getItemStackPathAtTarget(value);
            ItemStack item = retrieveItemStackAtPath(itemStackPath);
            guiBuilder.addNormalItem(slot, item);

            // Click action
            String clickActionPath = getClickActionPathAtTarget(value);

            if (config.isSet(clickActionPath))  {
                CustomClick customClick = retrieveClickActionAtPath(clickActionPath);
                guiBuilder.addCustomClick(slot, customClick);
            }
        }

        return guiBuilder.build();
    }

    private Gui.GuiBuilder getGuiBuilderFromFile() {
        String title = config.getString("Title");
        int size = config.getInt("Size");
        return new Gui.GuiBuilder(title, size);
    }

    private ItemStack retrieveItemStackAtPath(String path) {
        String name = config.getString(path + "Name");
        List<String> lore = config.getStringList(path + "Lore");
        List<String> flags = config.getStringList(path + "Flags");
        List<String> enchants = config.getStringList(path + "Enchants");
        Material material = Material.valueOf(Objects.requireNonNull(config.getString(path + "Material")).toUpperCase());
        String headValue = config.getString(path + "HeadValue");
        int amount = config.getInt(path + "Amount");

        ItemStack builderItem;

        // If the hd api exists and the head value is configured
        if (GuiCorePlugin.getInstance().isHeadDataBaseApiEnabled() && headValue != null) {
            builderItem = GuiCorePlugin.getInstance().getHeadDatabaseApi().getItemHead(headValue);

            if (builderItem == null) {
                GuiCorePlugin.getInstance().getLogger().warning("Head value in config is wrong! Path: " + path);
                return null;
            }
        } else {
            builderItem = new ItemStack(material);
        }

        ItemStackBuilder builder = ItemStackBuilder.of(builderItem);

        if (name != null) {
            builder.name(Util.trans(name));
        }

        if (!lore.isEmpty()) {
            builder.lore(updateStringListWithColors(lore));
        }

        if (!flags.isEmpty()) {
            for (String flag : flags)
                builder.flag(ItemFlag.valueOf(flag.toUpperCase()));
        }

        if (!enchants.isEmpty()) {
            for (String enchant : enchants) {
                String enchantName = enchant.substring(0, enchant.indexOf(":"));
                int enchantValue = Integer.parseInt(enchant.substring(enchant.lastIndexOf(":") + 1));
                builder.enchant(Objects.requireNonNull(Enchantment.getByName(enchantName)), enchantValue);
            }
        }

        if (amount > 0)
            builder.amount(amount);

        return builder.build();
    }

    private CustomClick retrieveClickActionAtPath(String path) {
        // Commands
        List<String> playerCommands = config.getStringList(path + "PlayerCommands");
        List<String> consoleCommands = config.getStringList(path + "ConsoleCommands");

        // Settings
        String sound = config.getString(path + "Sound");
        String permission = config.getString(path + "Permission");
        String message = config.getString(path + "Message");
        boolean closeInventoryOnClick = config.getBoolean(path + "CloseInventory");

        // Cost
        double moneyCost = config.getInt(path + "MoneyCost");
        List<String> itemKey = config.getStringList(path + "ItemKeys");
        CustomCost customCost;

        if (!itemKey.isEmpty()) {
            List<Pair<String, Integer>> pairList = new ArrayList<>();

            for (String string : itemKey) {
                String key = string.substring(0, string.indexOf(":"));
                int value = Integer.parseInt(string.substring(string.lastIndexOf(":") + 1));
                pairList.add(new Pair<>(key, value));
            }

            customCost = new CustomCost(moneyCost, pairList);
        } else {
            customCost = new CustomCost(moneyCost);
        }

        return CustomClick.create()
                .playerCommands(playerCommands)
                .consoleCommands(consoleCommands)
                .sound(sound)
                .permission(permission)
                .message(message)
                .closeOnClick(closeInventoryOnClick)
                .customCost(customCost)
                .build();
    }

    private Set<String> getConfigurationSectionValues() {
        return Objects.requireNonNull(config.getConfigurationSection(SECTION)).getKeys(false);
    }

    private List<String> updateStringListWithColors(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (String string : list)
            newList.add(Util.trans(string));

        return newList;
    }
}
