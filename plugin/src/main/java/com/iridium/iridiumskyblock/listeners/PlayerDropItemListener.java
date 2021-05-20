package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Optional;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getItemDrop().getLocation());

        if (!island.isPresent()) return;
        if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer()), IridiumSkyblock.getInstance().getPermissions().dropItems, "dropItems")) {
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotDropItems.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

}
