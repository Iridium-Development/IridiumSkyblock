package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.FIRE_SPREAD);
            if (!leafDecaySettings.getBooleanValue()) {
                event.setCancelled(true);
            }
        });
    }

}
