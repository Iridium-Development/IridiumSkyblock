package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.Optional;

public class BlockBreakListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (!island.isPresent()) return;

        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BLOCK_BREAK)) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        } else if (material.equals(XMaterial.SPAWNER) && !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.SPAWNERS)) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotMineSpawners.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockBreak(BlockBreakEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());

        user.getIsland().ifPresent(island -> {
            BlockData blockData = event.getBlock().getBlockData();
            if (blockData instanceof Ageable) {
                Ageable ageable = (Ageable) blockData;
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "MINE", material.name(), 1);
                }
            } else {
                IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "MINE", material.name(), 1);
            }

            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island, "experience");
            if (islandBooster.isActive()) {
                event.setExpToDrop(event.getExpToDrop() * 2);
            }
        });

        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            IslandBlocks islandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material);
            if (islandBlocks.getAmount() > 0) {
                islandBlocks.setAmount(islandBlocks.getAmount() - 1);
            }
            if (event.getBlock().getState() instanceof CreatureSpawner) {
                CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
                IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, creatureSpawner.getSpawnedType());
                if (islandSpawners.getAmount() > 0) {
                    islandSpawners.setAmount(islandSpawners.getAmount() - 1);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting) {
            Player remover = null;
            if (event.getRemover() instanceof Projectile && ((Projectile) event.getRemover()).getShooter() instanceof Player) {
                remover = (Player) ((Projectile) event.getRemover()).getShooter();
            } else if (event.getRemover() instanceof Player) {
                remover = (Player) event.getRemover();
            }

            if (remover == null) return;
            Hanging hangingEntity = event.getEntity();
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(remover);
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(hangingEntity.getLocation());
            if (!island.isPresent()) return;

            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BLOCK_BREAK)) {
                event.setCancelled(true);
                remover.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }

}
