package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalCreateListener implements Listener {
    @EventHandler
    public void onPortalCreate(PortalCreateEvent event){
        if (event.getWorld().equals(IridiumSkyblockAPI.getInstance().getEndWorld()) && event.getBlocks().stream().anyMatch(blockState -> blockState.getType().equals(Material.OBSIDIAN))) {
            event.setCancelled(true);
        }
    }
}
