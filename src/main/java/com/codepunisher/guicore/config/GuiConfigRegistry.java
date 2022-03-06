package com.codepunisher.guicore.config;

import com.codepunisher.guicore.GuiCorePlugin;
import com.codepunisher.guicore.models.Gui;

import java.io.File;

/**
 * Registers guis from all files,
 * stores, and registers command
 */
public final class GuiConfigRegistry {
    /**
     * Iterates through every file, gets
     * the gui object, and stores it
     */
    public static void registerAllGuis(GuiCorePlugin plugin) {
        File path = plugin.getDataFolder().getAbsoluteFile();
        File[] files = path.listFiles();

        if (files == null || files.length == 0) return;

        for (File file : files) {
            if (!file.getName().endsWith(".yml"))
                continue;

            GuiConfigRetriever configRetriever = new GuiConfigRetriever(file.getName());

            // Making sure the command is set (otherwise not initializing gui)
            String command = configRetriever.getConfig().getString("Command");

            if (command == null || command.isEmpty())
                continue;

            Gui gui = configRetriever.retrieveGuiFromFile();
            plugin.getGuiCommands().registerCommandToOpenGui(command, gui);
        }
    }
}
