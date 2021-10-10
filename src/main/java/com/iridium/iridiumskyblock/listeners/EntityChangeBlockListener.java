package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (SettingType.ENDERMAN_GRIEF.getFeactureValue().equalsIgnoreCase("false")) return;
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            if (event.getEntityType() == EntityType.ENDERMAN) {
                IslandSetting endermanGriefSettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.ENDERMAN_GRIEF);
                if (!endermanGriefSettings.getBooleanValue()) {
                    event.setCancelled(true);
                }
            }
        });
    }

}
