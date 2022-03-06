package com.codepunisher.guicore.config;

import com.codepunisher.guicore.models.CustomClick;
import com.codepunisher.guicore.models.CustomCost;
import com.codepunisher.guicore.models.Gui;
import com.mcaim.core.item.ItemBuild;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        int amount = config.getInt(path + "Amount");

        ItemBuild builder = ItemBuild.of(material);

        if (name != null)
            builder.name(name);

        if (!lore.isEmpty())
            builder.lore(lore);

        if (!flags.isEmpty()) {
            for (String flag : flags)
                builder.flag(ItemFlag.valueOf(flag.toUpperCase()));
        }

        if (!enchants.isEmpty()) {
            for (String enchant : enchants) {
                String enchantName = enchant.substring(0, enchant.indexOf(":"));
                int enchantValue = Integer.parseInt(enchant.substring(enchant.lastIndexOf(":") + 1));
                builder.enchant(Enchantment.getByName(enchantName), enchantValue);
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
        String itemKey = config.getString(path + "ItemKeyCost");
        CustomCost customCost;

        if (itemKey != null) {
            String key = itemKey.substring(0, itemKey.indexOf(":"));
            int value = Integer.parseInt(itemKey.substring(itemKey.lastIndexOf(":") + 1));
            customCost = new CustomCost(moneyCost, key, value);
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
}
