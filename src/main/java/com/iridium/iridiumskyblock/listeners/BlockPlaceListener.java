package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

}
