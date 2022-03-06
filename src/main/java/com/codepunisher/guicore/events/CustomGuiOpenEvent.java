package com.codepunisher.guicore.events;

import com.codepunisher.guicore.models.Gui;
import com.mcaim.core.util.CoreEvent;
import org.bukkit.entity.Player;

public final class CustomGuiOpenEvent extends CoreEvent {
    private final Player player;
    private final Gui gui;

    public CustomGuiOpenEvent(Player player, Gui gui) {
        this.player = player;
        this.gui = gui;
    }

    public Player getPlayer() { return player; }
    public Gui getGui() { return gui; }
}
