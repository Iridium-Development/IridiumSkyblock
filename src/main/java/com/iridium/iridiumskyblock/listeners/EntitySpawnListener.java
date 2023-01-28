package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntitySpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island -> {
            IslandSetting mobSpawnSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.MOB_SPAWN);

            // Exception for ArmorStands because they're a LivingEntity
            if (!mobSpawnSetting.getBooleanValue() && event.getEntity() instanceof LivingEntity && event.getEntityType() != EntityType.ARMOR_STAND) {
                event.setCancelled(true);
                return;
            }

            if (!IridiumSkyblock.getInstance().getIslandManager().checkEntityLimit(island, event.getEntity())) {
                int limitUpgradeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island, "entitylimit").getLevel();
                int entityLimit = IridiumSkyblock.getInstance().getUpgrades().entityLimitUpgrade.upgrades.get(limitUpgradeLevel).limits.getOrDefault(event.getEntityType(), 0);

                for (User user: island.getMembers()) {
                    Player player = user.getPlayer();
                    if (player == null || !player.isOnline())
                        continue;
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().entityLimitReached
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%limit%", String.valueOf(entityLimit)).replace("%entity%", WordUtils.capitalizeFully(event.getEntityType().name().toLowerCase().replace("_", " ")))));
                }
                event.setCancelled(true);
            } else {
                event.getEntity().setMetadata("island_spawned", new FixedMetadataValue(IridiumSkyblock.getInstance(), island.getId()));
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (IridiumSkyblock.getInstance().getIslandManager().isIslandEnd(event.getEntity().getWorld())) {
            if (event.getEntityType().equals(EntityType.ENDER_DRAGON) && event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DEFAULT)) {
                event.setCancelled(true);
            }
        }
    }

}
