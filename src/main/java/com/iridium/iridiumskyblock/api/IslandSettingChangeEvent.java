package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.Island;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class IslandSettingChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Island island;
    private final String newValue;
    private final String setting;

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
