package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (island.isPresent()) {
            XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().blockBreak)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else if (material.equals(XMaterial.SPAWNER) && !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().spawners)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotMineSpawners.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else if (IridiumSkyblock.getInstance().getBlockValues().blockValues.containsKey(material)) {
                IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island.get(), material).ifPresent(islandBlocks -> {
                    if (islandBlocks.getAmount() <= 0) return;
                    islandBlocks.setAmount(islandBlocks.getAmount() - 1);
                    island.get().setValue(island.get().getValue() - IridiumSkyblock.getInstance().getBlockValues().blockValues.get(material));
                });
            }
        }
    }

}
