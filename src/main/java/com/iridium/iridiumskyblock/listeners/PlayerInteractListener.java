package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (event.getClickedBlock() != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getClickedBlock().getLocation());
            if (island.isPresent()) {
                if (event.getClickedBlock().getType().name().toLowerCase().contains("door")) {
                    if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().doors)) {
                        event.setCancelled(true);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenDoors.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                    if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().openContainers)) {
                        event.setCancelled(true);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenContainers.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            }
        }
    }

}
