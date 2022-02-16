package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (true) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
                if (!island.isInIsland(event.getToBlock().getLocation())) {
                    event.setCancelled(true);
                    return;
                }
                if (SettingType.LIQUID_FLOW.isFeactureValue()) {
                    IslandSetting liquidFlowSettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.LIQUID_FLOW);
                    if (!liquidFlowSettings.getBooleanValue()) {
                        event.setCancelled(true);
                    }
                }

            });
        }
    }

}
