package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class EntityDamageListener implements Listener {

    //TODO: Implement Peaceful, fix hurting with projectiles

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getEntity().getLocation());
        if (island.isPresent()) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblockAPI.getInstance().getUser(player), IridiumSkyblock.getInstance().getPermissions().killMobs)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtMobs.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }
    }
}
