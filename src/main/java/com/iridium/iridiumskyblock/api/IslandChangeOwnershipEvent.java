package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class IslandChangeOwnershipEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Island island;
    private final User oldOwner;
    private final User newOwner;

    public IslandChangeOwnershipEvent(Island island, User oldOwner, User newOwner) {
        this.island = island;
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
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
