package com.codepunisher.guicore.commands;

import com.codepunisher.guicore.events.CustomGuiOpenEvent;
import com.codepunisher.guicore.models.Gui;;
import me.lucko.helper.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class GuiCommands {
    public void registerCommandToOpenGui(String command, Gui gui) {
        Commands.create()
                .assertPlayer()
                .assertPermission(gui.getPermission())
                .handler((c) -> {
                    Player player = c.sender();

                    CustomGuiOpenEvent guiOpenEvent = new CustomGuiOpenEvent(player, gui);
                    Bukkit.getPluginManager().callEvent(guiOpenEvent);

                    if (guiOpenEvent.isCancelled())
                        return;

                    gui.open(player);
                }).register(command);
    }
}
