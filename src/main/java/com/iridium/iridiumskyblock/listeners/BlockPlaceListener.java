package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.StackedBlock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.StackedBlockGUI;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (island.isPresent()) {
            IslandRank islandRank = island.get().equals(user.getIsland().orElse(null)) ? user.getIslandRank() : IslandRank.VISITOR;
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), islandRank, IridiumSkyblock.getInstance().getPermissions().blockPlace)) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPlaceBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || block.getType() == Material.AIR) return;
        //TODO: make here with IridiumSkyBlockAPI#isIslandWorld when is added.
        if (!IridiumSkyblockAPI.getInstance().getWorld().equals(block.getWorld())) return;
        Location location = block.getLocation();
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Material material = block.getType();
        XMaterial xmaterial = XMaterial.matchXMaterial(material);
        int amount = player.getItemInHand().getAmount();
        if (event.getPlayer().isSneaking()) {
            event.setCancelled(true);
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(location);
            if (island.isPresent() && island.get().getMembers().contains(user)) {
                List<StackedBlock> stackedBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandStackedBlocks(island.get());
                Optional<StackedBlock> optionalStackedBlock = stackedBlocks.stream().filter(sBlock -> sBlock.getLocation().equals(location) && xmaterial.name().equalsIgnoreCase(sBlock.getMaterial())).findFirst();
                if (optionalStackedBlock.isPresent() && player.getItemInHand().getType() == Material.AIR) {
                    player.openInventory(new StackedBlockGUI(optionalStackedBlock.get()).getInventory());
                } else if (optionalStackedBlock.isPresent() && xmaterial.name().equalsIgnoreCase(player.getItemInHand().getType().name())) {
                    optionalStackedBlock.get().setAmountStacked(optionalStackedBlock.get().getAmountStacked() + amount);
                    player.sendMessage("Total = " + optionalStackedBlock.get().getAmountStacked());
                } else if (!optionalStackedBlock.isPresent() && xmaterial.name().equalsIgnoreCase(player.getItemInHand().getType().name())) {
                    IridiumSkyblock.getInstance().getDatabaseManager().getStackedBlocksList().add(new StackedBlock(island.get(), xmaterial.name(), amount, location));
                    player.sendMessage("Total = " + amount);
                }
            }
        }
    }
}