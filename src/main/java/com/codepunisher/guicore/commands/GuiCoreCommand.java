package com.codepunisher.guicore.commands;

import com.codepunisher.guicore.GuiCorePlugin;
import com.codepunisher.guicore.config.GuiConfigAdder;
import com.codepunisher.guicore.config.GuiConfigRegistry;
import com.mcaim.core.command.QuickCommand;
import com.mcaim.core.cooldown.Cooldown;
import com.mcaim.core.util.Util;
import org.bukkit.ChatColor;
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
        QuickCommand.create()
                .assertPlayer()
                .assertOp()
                .assertArgument("reload", sender -> {
                    GuiCorePlugin plugin = GuiCorePlugin.getInstance();
                    plugin.getGuiCommands().unRegisterAllCommands();
                    GuiConfigRegistry.registerAllGuis(plugin);
                    sender.sendMessage(ChatColor.YELLOW + "GuiCore has been reloaded!");
                })
                .assertArgument("snapshot", sender -> {
                    Player player = (Player) sender;
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

                    Cooldown.get(player, "SNAPSHOT").time(3).run(() -> {
                        GuiConfigAdder.addItemsToNewFile(inventory.getContents());
                        sender.sendMessage(ChatColor.YELLOW + "You took a snapshot of the chest in front of you! Go to your yml to finish configuring, then type /guicore reload!");
                    });
                    })
                .register("guicore", sender -> {
                    sender.sendMessage(Util.trans("&8&m------------------------"));
                    sender.sendMessage(Util.trans("&6&lGuiCore Commands"));
                    sender.sendMessage(Util.trans("&8&m------------------------"));
                    sender.sendMessage(Util.trans("&e/guicore &7- &fdisplays this menu"));
                    sender.sendMessage(Util.trans("&e/guicore reload &7- &fupdates all yml files"));
                    sender.sendMessage(Util.trans("&e/guicore snapshot &7- &fcreates gui from chest (must be looking at chest)" +
                    " This ONLY creates a snapshot of the gui and stores it in the yml! MUST finish setting up the yml file"));
                    sender.sendMessage(Util.trans("&8&m------------------------"));
        });
    }

    private boolean isEmpty(Inventory inventory) {
        for (ItemStack item : inventory.getContents())
            if (item != null)
                return false;

        return true;
    }
}
