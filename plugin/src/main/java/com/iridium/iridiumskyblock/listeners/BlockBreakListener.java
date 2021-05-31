package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());

        if (!island.isPresent()) {
            return;
        }

        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().blockBreak, "blockBreak")) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        } else if (material.equals(XMaterial.SPAWNER) && !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().spawners, "spawners")) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotMineSpawners.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
        IslandBlocks islandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island.get(), material);
        if (islandBlocks.getAmount() > 0) {
            islandBlocks.setAmount(islandBlocks.getAmount() - 1);
        }
        if (event.getBlock().getState() instanceof CreatureSpawner) {
            CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
            IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island.get(), creatureSpawner.getSpawnedType());
            if (islandSpawners.getAmount() > 0) {
                islandSpawners.setAmount(islandSpawners.getAmount() - 1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());

        island.ifPresent(value -> {
            IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "MINE:" + material.name(), 1);
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "experience");
            if (islandBooster.isActive()) {
                event.setExpToDrop(event.getExpToDrop() * 2);
            }
        });
    }

}
