package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ItemCraftListener implements Listener {

    @EventHandler
    public void onItemCraftEventMonitor(CraftItemEvent event) {
        int amount = event.isShiftClick() ? Arrays.stream(event.getInventory().getMatrix()).filter(Objects::nonNull).map(ItemStack::getAmount).sorted().findFirst().orElse(1) : 1;
        Player player = (Player) event.getWhoClicked();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                for (int i = 1; i <= mission.getMissions().size(); i++) {
                    String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                    XMaterial material = XMaterial.matchXMaterial(event.getRecipe().getResult().getType());
                    if (conditions[0].equals("CRAFT") && (conditions[1].equals(material.name()) || conditions[1].equals("ANY"))) {
                        IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island.get(), mission, key, i);
                        if (islandMission.getProgress() + amount > Integer.parseInt(conditions[2]) && islandMission.getProgress() < Integer.parseInt(conditions[2])) {
                            islandMission.setProgress(Integer.parseInt(conditions[2]));
                            //Check if Mission is completed
                        } else {
                            islandMission.setProgress(islandMission.getProgress() + amount);
                        }
                    }
                }
            }
        }
    }

}
