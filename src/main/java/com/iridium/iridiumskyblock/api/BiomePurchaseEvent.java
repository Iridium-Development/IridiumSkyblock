package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.biomes.BiomeItem;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class BiomePurchaseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @NotNull private final Player player;
    @NotNull private final BiomeItem biomeItem;

    public BiomePurchaseEvent(@NotNull Player player, @NotNull BiomeItem biomeItem) {
        this.player = player;
        this.biomeItem = biomeItem;
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
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
