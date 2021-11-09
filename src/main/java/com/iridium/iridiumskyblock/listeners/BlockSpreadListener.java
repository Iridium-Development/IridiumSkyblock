package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getSource().getType() == Material.FIRE && SettingType.FIRE_SPREAD.isFeactureValue()) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
                IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.FIRE_SPREAD);
                if (!leafDecaySettings.getBooleanValue()) {
                    event.setCancelled(true);
                }
            });
        }
    }

}
