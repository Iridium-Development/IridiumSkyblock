package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before an Island has been renamed.
 */
@Getter
public class IslandRenameEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Nullable private String islandName;
    @NotNull private final User user;

    public IslandRenameEvent(@NotNull User user, @Nullable String islandName) {
        this.islandName = islandName;
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

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * The name of the Island.<br>
     * null indicates that the name of the Player is used as the Island name
     * because it hasn't been set.
     *
     * @return the name of the Island or null
     */
    @Nullable
    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(@Nullable String islandName) {
        this.islandName = islandName;
    }

}