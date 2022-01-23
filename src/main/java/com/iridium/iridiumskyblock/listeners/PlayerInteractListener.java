package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PlayerInteractListener implements Listener {

    private final CooldownProvider<Player> cooldownProvider = CooldownProvider.newInstance(Duration.ofMillis(500));
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
        user.getIsland().ifPresent(island -> {
            if (event.getAction() != Action.PHYSICAL) {
                int islandCrystals = IridiumSkyblock.getInstance().getIslandManager().getIslandCrystals(event.getPlayer().getInventory().getItemInMainHand());
                if (islandCrystals > 0) {
                    // Required because Spigot likes to trigger this event for each hand which removes two items
                    if (event.getHand() == EquipmentSlot.HAND) {
                        int amount = event.getPlayer().getInventory().getItemInMainHand().getAmount();
                        if (amount == 1) {
                            event.getPlayer().getInventory().setItemInMainHand(null);
                        } else {
                            event.getPlayer().getInventory().getItemInMainHand().setAmount(amount - 1);
                        }

                        IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem);
                        islandBank.setNumber(islandBank.getNumber() + islandCrystals);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().bankDeposited
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%type%", IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.getDisplayName())
                                .replace("%amount%", String.valueOf(islandCrystals))));
                        event.setCancelled(true);
                    }
                }
            }
        });

        if (event.getClickedBlock() == null) return;
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getClickedBlock().getLocation()).ifPresent(island -> {
            XMaterial material = XMaterial.matchXMaterial(event.getClickedBlock().getType());
            String materialName = material.name();

            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.INTERACT)) {
                event.setCancelled(true);
                if (hasNoCooldown(player)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInteract.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
                return;
            }

            if (event.getAction() == Action.PHYSICAL && material == XMaterial.FARMLAND) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.TRAMPLE_CROPS)) {
                    event.setCancelled(true);
                    if (hasNoCooldown(player)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotTrampleCrops.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            } else if (materialName.contains("DOOR")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.DOORS)) {
                    event.setCancelled(true);
                    if (hasNoCooldown(player)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenDoors.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            } else if (event.getClickedBlock().getState() instanceof InventoryHolder) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.OPEN_CONTAINERS)) {
                    event.setCancelled(true);
                    if (hasNoCooldown(player)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotOpenContainers.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            } else if (redstoneMaterials.contains(material) || materialName.contains("BUTTON") || materialName.contains("PRESSURE_PLATE")) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.REDSTONE)) {
                    event.setCancelled(true);
                    if (hasNoCooldown(player)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUseRedstone.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            } else if (materialName.contains("MINECART") || materialName.contains("BOAT") || materialName.contains("EGG") || materialName.contains("BUCKET") || material == XMaterial.END_CRYSTAL || material == XMaterial.ARMOR_STAND) {
                if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, PermissionType.INTERACT_ENTITIES)) {
                    event.setCancelled(true);
                    if (hasNoCooldown(player)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotSpawnEntities.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                }
            }
        });
    }

    private boolean hasNoCooldown(Player player) {
        boolean cooldown = cooldownProvider.isOnCooldown(player);
        cooldownProvider.applyCooldown(player);
        return cooldown;
    }

    @EventHandler(ignoreCancelled = true)
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        // Not sure why this isn't included in PlayerInteractEntity but meh
        // Ry probably coded it cuz hes dum like that
        onPlayerInteractEntity(event);
    }

    @EventHandler(ignoreCancelled = true)
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
