package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (island.isPresent()) {
            XMaterial xMaterial = XMaterial.matchXMaterial(event.getBlock().getType());
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().blockPlace)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPlaceBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else if (IridiumSkyblock.getInstance().getBlockValues().blockValues.containsKey(xMaterial)) {
                Optional<IslandBlocks> optionalIslandBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island.get(), xMaterial);
                if (optionalIslandBlock.isPresent()) {
                    optionalIslandBlock.get().setAmount(optionalIslandBlock.get().getAmount() + 1);
                } else {
                    IslandBlocks islandBlocks = new IslandBlocks(island.get(), xMaterial);
                    islandBlocks.setAmount(1);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksList().add(islandBlocks);
                }
                island.get().setValue(island.get().getValue() + IridiumSkyblock.getInstance().getBlockValues().blockValues.get(xMaterial));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlaceEventMonitor(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        if (island.isPresent()) {
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                for (int i = 1; i <= mission.getMissions().size(); i++) {
                    String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                    if (conditions[0].equals("PLACE") && (conditions[1].equals(material.name()) || conditions[1].equals("ANY"))) {
                        IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island.get(), mission, key, i);
                        if (islandMission.getProgress() >= Integer.parseInt(conditions[2])) {
                            //Check if Mission is completed
                        } else {
                            islandMission.setProgress(islandMission.getProgress() + 1);
                        }
                    }
                }
            }
        }
    }

}
