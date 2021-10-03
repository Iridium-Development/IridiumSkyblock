package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.settings.IslandMobSpawn;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntitySpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island -> {
            IslandSetting mobSpawnSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.MOB_SPAWN);
            IslandMobSpawn mobSpawn = IslandMobSpawn.valueOf(mobSpawnSetting.getValue());
            switch (mobSpawn) {
                case ANIMALS:
                    if (!(event.getEntity() instanceof Animals)) {
                        event.setCancelled(true);
                    }
                    return;
                case MONSTERS:
                    if (!(event.getEntity() instanceof Monster)) {
                        event.setCancelled(true);
                    }
                    return;
                case NOTHING:
                    event.setCancelled(true);
                    return;
            }
            event.getEntity().setMetadata("island_spawned", new FixedMetadataValue(IridiumSkyblock.getInstance(), island.getId()));
        });
    }

}
