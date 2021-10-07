package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpreadListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            if (event.getSource().getType() != Material.FIRE) return;
            IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get("fire_spread");
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, "fire_spread", switchSetting.getDefaultValue());
            if (switchSetting.isEnabled() && switchSetting.getByName(islandSetting.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                event.setCancelled(true);
            }
        });
    }

}
