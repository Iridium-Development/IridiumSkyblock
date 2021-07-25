package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import java.util.Optional;

public class VehicleDamageListener implements Listener {

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() != null && event.getAttacker() instanceof Player) {
            Player attacker = (Player) event.getAttacker();
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getVehicle().getLocation());
            if (island.isPresent()) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(attacker), PermissionType.BLOCK_BREAK)) {
                    event.setCancelled(true);
                    attacker.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }
    }
}
