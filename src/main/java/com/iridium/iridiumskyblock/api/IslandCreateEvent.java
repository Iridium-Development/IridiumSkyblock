package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before an Island has been created.
 */
@Getter
public class IslandCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final String islandName;
    @NotNull private final User user;
    @NotNull private Schematics.SchematicConfig schematicConfig;

    public IslandCreateEvent(@NotNull User user, @NotNull String islandName, Schematics.@NotNull SchematicConfig schematicConfig) {
        this.islandName = islandName;
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

    public void setSchematicConfig(Schematics.@NotNull SchematicConfig schematicConfig) {
        this.schematicConfig = schematicConfig;
    }

}
