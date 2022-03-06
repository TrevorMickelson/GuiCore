package com.codepunisher.guicore.commands;

import com.codepunisher.guicore.events.CustomGuiOpenEvent;
import com.codepunisher.guicore.models.Gui;
import com.mcaim.core.command.CommandRegistry;
import com.mcaim.core.command.QuickCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public final class GuiCommands {
    private final LinkedList<String> commands = new LinkedList<>();

    public void registerCommandToOpenGui(String command, Gui gui) {
        commands.add(command);

        QuickCommand.create()
                .assertPlayer()
                .assertPermission(gui.getPermission())
                .register(command, sender -> {
                    Player player = (Player) sender;

                    CustomGuiOpenEvent guiOpenEvent = new CustomGuiOpenEvent(player, gui);
                    Bukkit.getPluginManager().callEvent(guiOpenEvent);

                    if (guiOpenEvent.isCancelled())
                        return;

                    gui.open(player);
                });
    }

    public void unRegisterAllCommands() {
        commands.forEach(CommandRegistry::unRegister);
    }
}
