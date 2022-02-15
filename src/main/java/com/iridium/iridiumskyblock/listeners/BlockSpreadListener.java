package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        if (event.getSource().getType() == Material.FIRE && SettingType.FIRE_SPREAD.isFeactureValue()) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation(), true).ifPresent(island -> {
                IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.FIRE_SPREAD);
                if (!leafDecaySettings.getBooleanValue()) {
                    event.setCancelled(true);
                }
            });
        }
    }

}
