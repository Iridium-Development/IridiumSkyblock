package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (event.getClickedBlock() != null) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getClickedBlock().getLocation());
            if (island.isPresent()) {
                XMaterial xMaterial = XMaterial.matchXMaterial(event.getClickedBlock().getType());
                if (xMaterial.name().contains("DOOR")) {
                    if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().doors)) {
                        event.setCancelled(true);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenDoors.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                    if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().openContainers)) {
                        event.setCancelled(true);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenContainers.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else if (xMaterial.equals(XMaterial.LEVER) || xMaterial.name().contains("BUTTON") || xMaterial.name().contains("PRESSURE_PLATE")) {
                    if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().redstone)) {
                        event.setCancelled(true);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseRedstone.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getRightClicked().getLocation());
        if (island.isPresent()) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().interactEntities)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInteractEntities.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }

}
