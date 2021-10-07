package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.settings.IslandMobSpawn;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntitySpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity) || event.getEntity() instanceof Player) return;
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation()).ifPresent(island -> {
            IslandSettingImpl mobSpawnSetting = IridiumSkyblock.getInstance().getSettingsList().get("mob_spawn");
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, "mob_spawn", mobSpawnSetting.getDefaultValue());
            Enum<?> mobSpawnType = mobSpawnSetting.getByName(islandSetting.getValue());
            if (mobSpawnSetting.isEnabled() && mobSpawnType.equals(IslandMobSpawn.MobSpawnTypes.NOTHING) ||
                    (mobSpawnType.equals(IslandMobSpawn.MobSpawnTypes.ANIMALS) && event.getEntity() instanceof Monster) ||
                    (mobSpawnType.equals(IslandMobSpawn.MobSpawnTypes.MONSTERS) && event.getEntity() instanceof Animals)) {
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
