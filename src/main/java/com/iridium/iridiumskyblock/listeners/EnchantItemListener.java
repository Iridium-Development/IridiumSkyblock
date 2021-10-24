package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EnchantItemListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorItemEnchant(EnchantItemEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEnchanter().getWorld())) return;

        Player player = event.getEnchanter();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        XMaterial material = XMaterial.matchXMaterial(event.getItem().getType());

        // Increment missions with the name of the enchanted item
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "ENCHANT:" + material.name(), 1));

        // Increment missions with the ANY identifier
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "ENCHANT:ANY", 1));

        // Checks all itemLists created in missions.yml
        for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
            // If the enchanted item matches one in the list
            // Increment missions with the name of the list as the identifier
            if (itemList.getValue().contains(material.name())) {
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "ENCHANT:" + itemList.getKey(), 1));
            }
        }
    }

}
