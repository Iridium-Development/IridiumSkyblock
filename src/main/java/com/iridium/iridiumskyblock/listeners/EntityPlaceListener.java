package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityPlaceListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityPlace(EntityPlaceEvent event) {
        EntityType entityType = event.getEntityType();
        if (entityType != EntityType.ARMOR_STAND && entityType != EntityType.ENDER_CRYSTAL) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getEntity().getLocation()).ifPresent(island -> {
                if (!IridiumSkyblock.getInstance().getIslandManager().checkEntityLimit(island, event.getEntity())) {
                    int limitUpgradeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island, "entitylimit").getLevel();
                    int entityLimit = IridiumSkyblock.getInstance().getUpgrades().entityLimitUpgrade.upgrades.get(limitUpgradeLevel).limits.getOrDefault(entityType, 0);
                    for (User user: island.getMembers()) {
                        Player player = user.getPlayer();
                        if (player == null || !player.isOnline())
                            continue;
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().entityLimitReached
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%limit%", String.valueOf(entityLimit)).replace("%entity%", WordUtils.capitalizeFully(entityType.name().toLowerCase().replace("_", " ")))));
                    }
                    event.setCancelled(true);
                } else {
                    event.getEntity().setMetadata("island_spawned", new FixedMetadataValue(IridiumSkyblock.getInstance(), island.getId()));
                }
            });
        }
    }

}
