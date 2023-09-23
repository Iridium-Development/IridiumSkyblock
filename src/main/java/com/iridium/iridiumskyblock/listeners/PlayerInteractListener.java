package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.PermissionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerInteractListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        Optional<Island> island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaLocation(event.getClickedBlock().getLocation());
        if (!island.isPresent()) return;
        if (!IridiumSkyblock.getInstance().getTeamManager().getTeamPermission(island.get(), user, PermissionType.BLOCK_BREAK)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return;
        }

        if (IridiumSkyblock.getInstance().getConfiguration().obsidianBucket
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.OBSIDIAN)
                && itemInHand.getType().equals(Material.BUCKET)) {

            event.getClickedBlock().setType(Material.AIR);

            if (itemInHand.getAmount() > 1) {
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                player.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET)).values().forEach(itemStack ->
                        player.getWorld().dropItem(player.getLocation(), itemStack)
                );
            } else {
                itemInHand.setType(Material.LAVA_BUCKET);
            }
        }

        if (IridiumSkyblock.getInstance().getConfiguration().endPortalPick
                && (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && player.isSneaking())
                && event.getClickedBlock().getType().equals(Material.END_PORTAL_FRAME)
                && (itemInHand.getType().name().contains("PICKAXE"))) {

            event.getClickedBlock().breakNaturally();

            player.getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(Material.END_PORTAL_FRAME));
            removeAdjacentBlocks(event.getClickedBlock().getLocation(), XMaterial.END_PORTAL.parseMaterial(), new AtomicInteger(9));
        }
    }

    public void removeAdjacentBlocks(Location location, Material targetMaterial, AtomicInteger iterationsLeft) {
        if (iterationsLeft.get() <= 0) {
            return;
        }

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    Location adjacentLocation = location.clone().add(xOffset, yOffset, zOffset);
                    Block block = adjacentLocation.getBlock();
                    if (block.getType() == targetMaterial) {
                        block.breakNaturally();
                        iterationsLeft.getAndDecrement();
                        removeAdjacentBlocks(adjacentLocation, targetMaterial, iterationsLeft);
                    }
                }
            }
        }
    }

}
