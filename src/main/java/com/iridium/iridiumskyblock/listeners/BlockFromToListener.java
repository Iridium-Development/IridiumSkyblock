package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
                if(!island.isInIsland(event.getToBlock().getLocation())){
                    event.setCancelled(true);
                }
                IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get("liquid_flow");
                IslandSetting liquidFlowSettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, "liquid_flow", switchSetting.getDefaultValue());
                if (switchSetting.isEnabled() && switchSetting.getByName(liquidFlowSettings.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                    event.setCancelled(true);
                }
            }
        });
    }

}
