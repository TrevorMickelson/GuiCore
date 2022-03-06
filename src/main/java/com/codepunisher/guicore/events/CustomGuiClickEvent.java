package com.codepunisher.guicore.events;

import com.codepunisher.guicore.models.CustomClick;
import com.mcaim.core.util.CoreEvent;
import org.bukkit.entity.Player;

public final class CustomGuiClickEvent extends CoreEvent {
    private final Player player;
    private final int slot;
    private final CustomClick customClick;

    public CustomGuiClickEvent(Player player, int slot, CustomClick customClick) {
        this.player = player;
        this.slot = slot;
        this.customClick = customClick;
    }

    public Player getPlayer() { return player; }
    public int getSlot() { return slot; }
    public CustomClick getCustomClick() { return customClick; }
}
