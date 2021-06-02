package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class UserDemoteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Island island;
    private final User user;
    private final IslandRank newRank;

    public UserDemoteEvent(Island island, User user, IslandRank newRank) {
        this.island = island;
        this.user = user;
        this.newRank = newRank;
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
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
