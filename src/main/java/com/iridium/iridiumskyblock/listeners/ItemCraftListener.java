package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemCraftListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorItemCraft(CraftItemEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getWhoClicked().getWorld())) return;

        int amount = event.isShiftClick() ? Arrays.stream(event.getInventory().getMatrix())
                .filter(Objects::nonNull)
                .map(ItemStack::getAmount)
                .sorted()
                .findFirst()
                .orElse(1) * event.getRecipe().getResult().getAmount() : event.getRecipe().getResult().getAmount();

        Player player = (Player) event.getWhoClicked();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        XMaterial material = XMaterial.matchXMaterial(event.getRecipe().getResult().getType());

        island.ifPresent(value -> IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(value, "CRAFT", material.name(), amount));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack itemStack : event.getInventory().getMatrix()) {
            if (IridiumSkyblock.getInstance().getIslandManager().getIslandCrystals(itemStack) > 0) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                break;
            }
        }
    }

}
