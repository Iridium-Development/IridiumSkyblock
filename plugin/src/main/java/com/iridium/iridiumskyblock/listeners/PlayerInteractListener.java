package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
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
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        if (event.getClickedBlock() != null) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getClickedBlock().getLocation());
            if (!island.isPresent()) {
                return;
            }

            XMaterial material = XMaterial.matchXMaterial(event.getClickedBlock().getType());
            if (material.name().contains("DOOR")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().doors, "doors")) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenDoors.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().openContainers, "openContainers")) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenContainers.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (material.equals(XMaterial.LEVER) || material.name().contains("BUTTON") || material.name().contains("PRESSURE_PLATE")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().redstone, "redstone")) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseRedstone.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getRightClicked().getLocation());

        if (!island.isPresent()) {
            return;
        }

        if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().interactEntities, "interactEntities")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInteractEntities.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

}
