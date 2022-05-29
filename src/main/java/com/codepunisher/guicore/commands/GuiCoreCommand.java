package com.codepunisher.guicore.commands;

import com.codepunisher.guicore.GuiCorePlugin;
import com.codepunisher.guicore.config.GuiConfigAdder;
import com.codepunisher.guicore.config.GuiConfigRegistry;
import com.codepunisher.guicore.util.Util;
import me.lucko.helper.Commands;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GuiCoreCommand {
    public static void register() {
        new GuiCoreCommand().handleGuiCoreCommand();
    }

    private void handleGuiCoreCommand() {
        Commands.create()
                .assertPlayer()
                .assertOp()
                .handler(c -> {
                    c.sender().sendMessage(Util.trans("&8&m------------------------"));
                    c.sender().sendMessage(Util.trans("&6&lGuiCore Commands"));
                    c.sender().sendMessage(Util.trans("&8&m------------------------"));
                    c.sender().sendMessage(Util.trans("&e/guicore &7- &fdisplays this menu"));
                    c.sender().sendMessage(Util.trans("&e/guicore reload &7- &fupdates all yml files"));
                    c.sender().sendMessage(Util.trans("&e/guicore snapshot &7- &fcreates gui from chest (must be looking at chest)" +
                            " This ONLY creates a snapshot of the gui and stores it in the yml! MUST finish setting up the yml file"));
                    c.sender().sendMessage(Util.trans("&8&m------------------------"));
                }).register("guicore");

        Commands.create()
                .assertPlayer()
                .assertOp()
                .handler(c -> {
                    GuiCorePlugin plugin = GuiCorePlugin.getInstance();
                    GuiConfigRegistry.registerAllGuis(plugin);
                    c.sender().sendMessage(ChatColor.YELLOW + "GuiCore has been reloaded!");
                }).register("guicore-reload");

        Commands.create()
                .assertPlayer()
                .assertOp()
                .handler(c -> {
                    Player player = c.sender();
                    Block block = player.getTargetBlock(null, 5);

                    if (block.getType() != Material.CHEST) {
                        player.sendMessage(ChatColor.RED + "In order to take a snapshot, you must be looking at a chest!");
                        return;
                    }

                    Chest chest = (Chest) block.getState();
                    Inventory inventory = chest.getInventory();

                    // If empty
                    if (isEmpty(inventory)) {
                        player.sendMessage(ChatColor.RED + "That chest is empty! Must have contents in order to take a snapshot!");
                        return;
                    }

                    GuiConfigAdder.addItemsToNewFile(inventory.getContents());
                    player.sendMessage(ChatColor.YELLOW + "You took a snapshot of the chest in front of you! Go to your yml to finish configuring, then type /guicore reload!");
                }).register("guicore-snapshot");
    }

    private boolean isEmpty(Inventory inventory) {
        for (ItemStack item : inventory.getContents())
            if (item != null)
                return false;

        return true;
    }
}
