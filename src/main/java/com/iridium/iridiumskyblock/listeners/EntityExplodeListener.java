package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandSettingType;
import com.iridium.iridiumskyblock.settings.IslandSwitchSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
        if (list.isEmpty()) return;
        int islandId = list.get(0).asInt();
        IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island -> {
            IslandSettingImpl switchSetting = IridiumSkyblock.getInstance().getSettingsList().get(IslandSettingType.TNT_DAMAGE.getSettingKey());
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, IslandSettingType.TNT_DAMAGE.getSettingKey(), switchSetting.getDefaultValue());
            if (switchSetting.isEnabled() && switchSetting.getByName(islandSetting.getValue()).equals(IslandSwitchSetting.SwitchTypes.DISALLOWED)) {
                event.setCancelled(true);
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        });
    }

}
