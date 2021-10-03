package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            if (event.getSource().getType() != Material.FIRE) return;
            IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.FIRE_SPREAD);
            if (!leafDecaySettings.isAllowed()) {
                event.setCancelled(true);
            }
        });
    }

}
