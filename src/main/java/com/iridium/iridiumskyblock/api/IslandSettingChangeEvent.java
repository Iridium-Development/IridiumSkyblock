package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.Island;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 *  Called before an Island setting has been altered.
 */
@Getter
public class IslandSettingChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final Player player;
    @NotNull private final Island island;
    @NotNull private final SettingType settingType;
    @NotNull private String newValue;

    public IslandSettingChangeEvent(@NotNull Player player, @NotNull Island island, @NotNull SettingType settingType, @NotNull String newValue) {
        this.player = player;
        this.island = island;
        this.settingType = settingType;
        this.newValue = newValue;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setNewValue(@NotNull String newValue) {
        this.newValue = newValue;
    }

}
