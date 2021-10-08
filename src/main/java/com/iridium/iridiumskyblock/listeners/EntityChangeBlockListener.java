package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            if (event.getEntityType() == EntityType.ENDERMAN) {
                IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get(IslandSettingType.ENDERMAN_GRIEF.getSettingKey());
                IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.ENDERMAN_GRIEF.getSettingKey(), switchSetting.getDefaultValue());
                if (switchSetting.isEnabled() && switchSetting.getByName(islandSetting.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                    event.setCancelled(true);
                }
            }
        });
    }

}
