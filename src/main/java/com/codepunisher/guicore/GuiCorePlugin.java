package com.codepunisher.guicore;

import com.codepunisher.guicore.commands.GuiCommands;
import com.codepunisher.guicore.config.GuiConfigRegistry;
import com.codepunisher.guicore.commands.GuiCoreCommand;
import com.codepunisher.guicore.listeners.GuiClickListener;
import com.mcaim.core.configuration.ConfigFile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class GuiCorePlugin extends JavaPlugin {
    private static GuiCorePlugin guiCorePlugin;
    private GuiCommands guiCommands;
    private Economy economy;
    private boolean placeHolderAPI;

    @Override
    public void onEnable() {
        guiCorePlugin = this;
        loadExampleFile();
        guiCommands = new GuiCommands();

        // Apis
        economy = initEconomy();
        checkForPlaceHolderAPI();

        // Listeners
        getServer().getPluginManager().registerEvents(new GuiClickListener(), this);
        getServer().getPluginManager().registerEvents(new GuiClickListener(), this);

        // Command and guis
        GuiCoreCommand.register();
        GuiConfigRegistry.registerAllGuis(this);
    }

    @Override
    public void onDisable() {
    }

    private Economy initEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return null;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
            return null;

        return rsp.getProvider();
    }

    private void checkForPlaceHolderAPI() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeHolderAPI = true;
            return;
        }

        placeHolderAPI = false;
    }

    /**
     * Example file only loads if the
     * folder does not exist
     */
    private void loadExampleFile() {
        if (!getDataFolder().exists())
            new ConfigFile(this, "example.yml").saveDefaultConfig();
    }

    public GuiCommands getGuiCommands() { return guiCommands; }
    public static GuiCorePlugin getInstance() { return guiCorePlugin; }
    public Economy getEconomy() { return economy; }
    public boolean isPlaceHolderAPI() { return placeHolderAPI; }
}
