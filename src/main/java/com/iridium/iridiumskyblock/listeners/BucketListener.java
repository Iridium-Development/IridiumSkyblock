package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class BucketListener implements Listener {

    @EventHandler
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        onBucketEvent(event);
    }

    @EventHandler
    public void onBucketFillEvent(PlayerBucketFillEvent event) {
        onBucketEvent(event);
    }

    public void onBucketEvent(PlayerBucketEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlockClicked().getLocation());

        if (!island.isPresent())
            return;
        if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BUCKET)) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseBuckets.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        // I tested this on 1.17.1 so if it doesnt work on any version lower, they can suck it kekW

        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null)
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        final ItemStack handItem = event.getItem();
        if (handItem == null)
            return;

        if (handItem.getType() != Material.BUCKET)
            return;

        if (clickedBlock.getType() != Material.OBSIDIAN)
            return;

        final IslandManager manager = IridiumSkyblock.getInstance().getIslandManager();

        final Optional<Island> islandOptional = manager.getIslandViaLocation(event.getClickedBlock().getLocation());
        if (!islandOptional.isPresent())
            return;

        final User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        if (!manager.getIslandPermission(islandOptional.get(), user, PermissionType.BUCKET))
            return;


        event.getClickedBlock().setType(Material.AIR);
        // Change item in hand a ticket later to prevent lava duplication
        Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> event.getPlayer().getInventory().setItem(event.getHand(), new ItemStack(Material.LAVA_BUCKET)), 1);

        event.setCancelled(true);
    }

}