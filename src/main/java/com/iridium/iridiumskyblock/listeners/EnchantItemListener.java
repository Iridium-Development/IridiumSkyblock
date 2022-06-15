package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Optional;

public class EnchantItemListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorItemEnchant(EnchantItemEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEnchanter().getWorld())) return;

        Player player = event.getEnchanter();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        IridiumMaterial material = IridiumMaterial.matchXMaterial(event.getItem().getType());

        island.ifPresent(value -> IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(value, "ENCHANT", material.name(), 1));
    }

}
