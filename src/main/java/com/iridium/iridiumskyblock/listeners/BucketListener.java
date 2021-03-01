package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

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
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlockClicked().getLocation());
        if (island.isPresent()) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().bucket)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseBuckets.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }

}