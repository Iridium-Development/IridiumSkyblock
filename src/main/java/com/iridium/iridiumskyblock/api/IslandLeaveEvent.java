package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class IslandLeaveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Island fromIsland;
    private final User user;

    public IslandLeaveEvent(Island fromIsland, User user) {
        this.fromIsland = fromIsland;
        this.user = user;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}