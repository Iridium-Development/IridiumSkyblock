package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a user demoted another user.
 */
@Getter
public class UserDemoteEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final Island island;
    @NotNull private final User user;
    @NotNull private final IslandRank newRank;

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
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
