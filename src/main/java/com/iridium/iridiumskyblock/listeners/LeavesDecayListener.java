package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeavesDecayListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get(IslandSettingType.LEAF_DECAY.getSettingKey());
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.LEAF_DECAY.getSettingKey(), switchSetting.getDefaultValue());
            if (switchSetting.isEnabled() && switchSetting.getByName(islandSetting.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                event.setCancelled(true);
            }
        });
    }

}
