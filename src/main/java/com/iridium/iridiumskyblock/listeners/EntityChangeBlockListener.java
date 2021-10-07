package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
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
                IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get("enderman_grief");
                IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, "enderman_grief", switchSetting.getDefaultValue());
                if (switchSetting.isEnabled() && switchSetting.getByName(islandSetting.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                    event.setCancelled(true);
                }
            }
        });
    }

}
