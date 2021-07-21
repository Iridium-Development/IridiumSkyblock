package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.IslandSpawners;
import com.iridium.iridiumskyblock.database.User;
import org.apache.commons.lang.WordUtils;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Objects;
import java.util.Optional;

public class BlockPlaceListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (!island.isPresent()) {
            World world = event.getBlock().getLocation().getWorld();
            if (Objects.equals(world, IridiumSkyblock.getInstance().getIslandManager().getWorld()) || Objects.equals(world, IridiumSkyblock.getInstance().getIslandManager().getNetherWorld()) || Objects.equals(world, IridiumSkyblock.getInstance().getIslandManager().getEndWorld())) {
                if (!user.isBypass()) event.setCancelled(true);
            }
            return;
        }

        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BLOCK_PLACE)) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPlaceBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        IslandBlocks islandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island.get(), material);
        
        if(IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.enabled) {
            int limitUpgradeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "blocklimit").getLevel();
            int blockLimit = IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.get(limitUpgradeLevel).limits.getOrDefault(material, 0);

            if (blockLimit > 0 && islandBlocks.getAmount() >= blockLimit) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().blockLimitReached
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%limit%", String.valueOf(blockLimit)).replace("%block%", WordUtils.capitalizeFully(material.name().toLowerCase().replace("_", " ")))));
                event.setCancelled(true);
                return;
            }
        }

        islandBlocks.setAmount(islandBlocks.getAmount() + 1);

        if (event.getBlock().getState() instanceof CreatureSpawner) {
            CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
            IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island.get(), creatureSpawner.getSpawnedType());
            islandSpawners.setAmount(islandSpawners.getAmount() + 1);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockPlace(BlockPlaceEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "PLACE:" + material.name(), 1));
    }

}
