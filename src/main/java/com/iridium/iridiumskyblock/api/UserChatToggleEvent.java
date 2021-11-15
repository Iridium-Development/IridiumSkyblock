package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a user toggles the Island chat using /is chat.
 */
@Getter
public class UserChatToggleEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final boolean newChatState;
    @NotNull private final User user;

    public UserChatToggleEvent(@NotNull User user, boolean newState) {
        this.user = user;
        this.newChatState = newState;
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
