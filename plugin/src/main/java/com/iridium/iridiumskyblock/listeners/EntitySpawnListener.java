package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntitySpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island ->
                IridiumSkyblock.getInstance().getIslandManager().getEntities(island, event.getEntity().getWorld()).thenAccept(entities -> {
                    List<Entity> islandEntities = entities.stream().filter(entity -> entity instanceof LivingEntity && !(entity instanceof Player)).collect(Collectors.toList());
                    if (islandEntities.size() > IridiumSkyblock.getInstance().getConfiguration().maxIslandEntities) {
                        event.getEntity().remove();
                    }
                })
        );
    }

}
