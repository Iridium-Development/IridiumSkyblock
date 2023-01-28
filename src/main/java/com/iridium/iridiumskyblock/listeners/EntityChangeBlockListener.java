package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityChangeBlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.SILVERFISH) {
            List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
            if (list.isEmpty()) return;
            int islandId = list.get(0).asInt();
            IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island -> IridiumSkyblock.getInstance().getIslandManager().onEntityRemoved(island, event.getEntity()));
        }

        if (event.getEntityType() == EntityType.ENDERMAN) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
                IslandSetting endermanGriefSettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.ENDERMAN_GRIEF);
                if (!endermanGriefSettings.getBooleanValue()) {
                    event.setCancelled(true);
                }
            });
        }
    }

}
