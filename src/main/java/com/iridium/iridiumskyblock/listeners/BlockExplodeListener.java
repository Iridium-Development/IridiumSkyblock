package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandSetting tntExplosion = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.TNT_DAMAGE);
            if (!tntExplosion.isAllowed()) {
                event.setCancelled(true);
                return;
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        });
    }


}
