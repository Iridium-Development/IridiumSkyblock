package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PlayerInteractListener implements Listener {

    private final Set<XMaterial> redstoneMaterials = new HashSet<>(Arrays.asList(
            XMaterial.REPEATER,
            XMaterial.COMPARATOR,
            XMaterial.DAYLIGHT_DETECTOR,
            XMaterial.LEVER,
            XMaterial.NOTE_BLOCK,
            XMaterial.TRIPWIRE
    ));

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        if (event.getClickedBlock() != null) {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getClickedBlock().getLocation());
            if (!optionalIsland.isPresent()) {
                return;
            }

            XMaterial material = XMaterial.matchXMaterial(event.getClickedBlock().getType());
            String materialName = material.name();
            Island island = optionalIsland.get();

            if (event.getAction() == Action.PHYSICAL && material == XMaterial.FARMLAND) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.BLOCK_BREAK)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (materialName.contains("DOOR")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.DOORS)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenDoors.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.OPEN_CONTAINERS)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenContainers.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (redstoneMaterials.contains(material) || materialName.contains("BUTTON") || materialName.contains("PRESSURE_PLATE")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.REDSTONE)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseRedstone.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else if (materialName.contains("MINECART") || materialName.contains("BOAT") || materialName.contains("EGG") || materialName.contains("BUCKET") || material == XMaterial.END_CRYSTAL) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.BLOCK_PLACE)) {
                    event.setCancelled(true);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getRightClicked().getLocation());
        if (!island.isPresent()) return;

        if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.INTERACT_ENTITIES)) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInteractEntities.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

}
