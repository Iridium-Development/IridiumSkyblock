package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before an Island has been regenerated using /is regen.
 */
@Getter
public class IslandRegenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final Island island;
    @NotNull private final User user;
    @NotNull private final Schematics.SchematicConfig schematicConfig;

    public IslandRegenEvent(@NotNull Island island, @NotNull User user, Schematics.@NotNull SchematicConfig schematicConfig) {
        this.island = island;
        this.user = user;
        this.schematicConfig = schematicConfig;
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
