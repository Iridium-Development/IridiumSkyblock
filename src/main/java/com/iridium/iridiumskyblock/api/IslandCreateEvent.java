package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter

public class IslandCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Nullable
    private String islandName;
    @NotNull
    private final User user;
    @NotNull
    private Schematics.SchematicConfig schematicConfig;

    public IslandCreateEvent(@NotNull User user, @Nullable String islandName, Schematics.@NotNull SchematicConfig schematicConfig) {
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

    /**
     *
     * Sets the name of the Island.<br>
     * set it to null to default to the player's name
     *
     * @param islandName The name of the island
     */
    public void setIslandName(@Nullable String islandName) {
        this.islandName = islandName;
    }

    /**
     * Sets the schematic of the new island
     *
     * @param schematicConfig The schematic configuration of the island
     */

    public void setSchematicConfig(Schematics.@NotNull SchematicConfig schematicConfig) {
        this.schematicConfig = schematicConfig;
    }
}
