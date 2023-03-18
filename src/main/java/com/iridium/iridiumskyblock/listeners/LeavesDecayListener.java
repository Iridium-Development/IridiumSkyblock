package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (IridiumSkyblock.getInstance().getConfiguration().performance.disableLeavesDecayCheck) return;
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.LEAF_DECAY);
            if (!leafDecaySettings.getBooleanValue()) {
                event.setCancelled(true);
            }
        });
    }

}
