package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before a user joins a new Island.
 */
@Getter
public class UserJoinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final Island island;
    @NotNull private final User user;
    @NotNull private final User inviter;

    public UserJoinEvent(Island island, User user, @Nullable User inviter) {
        this.island = island;
        this.user = user;
        this.inviter = inviter;
    }

    public boolean isJoinedByInvite() {
        return inviter != null;
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
