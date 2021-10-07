package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get("tnt_damage");
            IslandSetting tntExplosion = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, "tnt_damage", switchSetting.getDefaultValue());
            if (switchSetting.isEnabled() && switchSetting.getByName(tntExplosion.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                event.setCancelled(true);
                return;
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        });
    }


}
