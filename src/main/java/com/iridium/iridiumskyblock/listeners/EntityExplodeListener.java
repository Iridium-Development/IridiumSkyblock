package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
        if (list.isEmpty()) return;
        int islandId = list.get(0).asInt();
        IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island -> {
            IslandSetting tntExplosion = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.TNT_DAMAGE);
            if (SettingType.TNT_DAMAGE.getFeactureValue().equalsIgnoreCase("true") && !tntExplosion.getBooleanValue()) {
                event.setCancelled(true);
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        });
    }

}
