package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntitySpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island -> {
            IslandSetting mobSpawnSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.MOB_SPAWN);
            if (!mobSpawnSetting.getBooleanValue()) {
                event.setCancelled(true);
                return;
            }
            event.getEntity().setMetadata("island_spawned", new FixedMetadataValue(IridiumSkyblock.getInstance(), island.getId()));
        });
    }

}
