package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntitySpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island -> {
            EntityType entityType = event.getEntityType();
            int mobLimit = IridiumSkyblock.getInstance().getConfiguration().islandMobLimits.getOrDefault(entityType, 0);
            if (mobLimit > 0) {
                List<World> islandWorlds = Arrays.asList(
                        IridiumSkyblock.getInstance().getIslandManager().getWorld(),
                        IridiumSkyblock.getInstance().getIslandManager().getNetherWorld(),
                        IridiumSkyblock.getInstance().getIslandManager().getEndWorld());
                List<Entity> entities = new ArrayList<>();
                islandWorlds.stream().map(World::getEntities).forEach(entities::addAll);
                long amountEntity = entities.stream()
                        .filter(entity -> entityType.equals(entity.getType()) && island.isInIsland(entity.getLocation()))
                        .count();

                if (amountEntity >= mobLimit) {
                    event.setCancelled(true);
                    return;
                }
            }
            IslandSetting mobSpawnSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.MOB_SPAWN);
            if (!mobSpawnSetting.getBooleanValue()) {
                event.setCancelled(true);
                return;
            }
            event.getEntity().setMetadata("island_spawned", new FixedMetadataValue(IridiumSkyblock.getInstance(), island.getId()));
        });
    }

}
