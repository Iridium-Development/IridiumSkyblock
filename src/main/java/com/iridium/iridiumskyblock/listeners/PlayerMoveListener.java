package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.LostItems;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.enhancements.VoidEnhancementData;
import com.iridium.iridiumteams.utils.LocationUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(event.getPlayer());

        user.getCurrentIsland().ifPresent(island -> {
            if (!(event.getPlayer().getLocation().getY() < LocationUtils.getMinHeight(event.getPlayer().getWorld()))) {
                return;
            }

            VoidEnhancementData voidEnhancementData = IridiumSkyblock.getInstance()
                    .getEnhancements().voidEnhancement.levels
                    .get(IridiumSkyblock.getInstance().getTeamManager().getTeamEnhancement(island, "void").getLevel());

            if (voidEnhancementData == null || !voidEnhancementData.enabled) {
                return;
            }

            if (!IridiumSkyblock.getInstance().getTeamManager().teleport(event.getPlayer(), island.getHome(), island)) {
                return;
            }

            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().voidTeleport
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));

            HashMap<ItemStack, Integer> lostItemsMap = new HashMap<>();
            if (voidEnhancementData.itemLossChance > 0) {
                for (ItemStack item : event.getPlayer().getInventory().getContents()) {
                    if (item == null) {
                        continue;
                    }

                    ItemStack originalItem = item.clone();

                    for (int i = 0; i < item.getAmount(); i++) {
                        if (Math.random() * 100 > voidEnhancementData.itemLossChance) {
                            continue;
                        }

                        lostItemsMap.put(originalItem, lostItemsMap.getOrDefault(originalItem, 0) + 1);
                    }

                    item.setAmount(item.getAmount() - lostItemsMap.getOrDefault(originalItem, 0));
                }

                if (lostItemsMap.isEmpty()) {
                    return;
                }

                IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().addEntry(new LostItems(
                        event.getPlayer().getUniqueId(), lostItemsMap.keySet().toArray(new ItemStack[0])));
                IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().save();

                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance()
                        .getMessages().voidLostItems
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%items%", String.join(", ", Arrays
                                .stream(lostItemsMap.keySet().toArray(new ItemStack[0]))
                                .map(item -> IridiumSkyblock.getInstance().getMessages().itemsString
                                        .replace("%amount%", lostItemsMap.get(item).toString())
                                        .replace("%item_name%", item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "%type%")
                                        .replace("%type%", item.getType().name().trim().replace("_", " ")))
                                .toArray(String[]::new)))
                ));

                System.out.println(String.join(", ", Arrays.stream(IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().getLostItems(event.getPlayer().getUniqueId()).get(0).getItems())
                        .map(item -> item.getType().name().trim().replace("_", " "))
                        .toArray(String[]::new)));
            }
        });
    }
}
