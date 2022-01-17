package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class BucketListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        onBucketEvent(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFillEvent(PlayerBucketFillEvent event) {
        onBucketEvent(event);
    }

    public void onBucketEvent(PlayerBucketEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());

        if (!island.isPresent()) {
            if (IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlockClicked().getWorld())) {
                if (!user.isBypassing()) event.setCancelled(true);
            }
            return;
        }
        if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BUCKET)) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseBuckets.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getPlayer().getWorld())) return;
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!(IridiumSkyblock.getInstance().getConfiguration().obsidianBucket
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.OBSIDIAN)
                && itemInHand.getType().equals(Material.BUCKET))) {
            return;
        }

        Location location = event.getClickedBlock().getLocation();
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(location);
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        if (island.isPresent()) {
            if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BUCKET)) {
                event.getClickedBlock().setType(Material.AIR);
                if (itemInHand.getAmount() > 1) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    if (InventoryUtils.hasEmptySlot(player.getInventory())) {
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
                    } else {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inventoryFull));
                        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.LAVA_BUCKET));
                    }
                } else {
                    itemInHand.setType(Material.LAVA_BUCKET);
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }
}
