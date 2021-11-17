package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.database.User;
import gyurix.outpost.OutpostAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDeathListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorEntityDeath(EntityDeathEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEntity().getWorld())) return;

        Player player = event.getEntity().getKiller();
        if (player == null) return;

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        island.ifPresent(value -> {
            IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "KILL:" + event.getEntityType().name(), 1);
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "experience");
            if (islandBooster.isActive()) {
                event.setDroppedExp(event.getDroppedExp() * 2);
            }

            IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "mobDropMultiplier");
            int mobDropMultiplier = IridiumSkyblock.getInstance().getUpgrades().mobDropMultiplierUpgrade.upgrades.get(islandUpgrade.getLevel()).amount;
            if(OutpostAPI.isActive(player)) {
                mobDropMultiplier++;
                event.setDroppedExp(event.getDroppedExp() * 2);
            }

            if(mobDropMultiplier > 1) {
                List<ItemStack> drops = new ArrayList(event.getDrops());
                event.getDrops().clear();
                int finalMobDropMultiplier = mobDropMultiplier;
                drops.forEach(drop -> {
                    ItemStack dropClone = drop.clone();
                    dropClone.setAmount(dropClone.getAmount() * finalMobDropMultiplier);
                    event.getDrops().add(dropClone);
                });
            }
        });
    }

}
