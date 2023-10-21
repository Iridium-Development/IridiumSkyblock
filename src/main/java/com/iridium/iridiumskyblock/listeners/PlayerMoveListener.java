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

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(event.getPlayer());

        user.getCurrentIsland().ifPresent(island -> {
            if (event.getPlayer().getLocation().getY() >= LocationUtils.getMinHeight(event.getPlayer().getWorld())) return;

            VoidEnhancementData voidEnhancementData = IridiumSkyblock.getInstance()
                    .getEnhancements().voidEnhancement.levels
                    .get(IridiumSkyblock.getInstance().getTeamManager().getTeamEnhancement(island, "void").getLevel());

            if (voidEnhancementData == null || !voidEnhancementData.enabled) return;

            if (!IridiumSkyblock.getInstance().getTeamManager().teleport(event.getPlayer(), event.getPlayer().getLocation(), island)) return;

            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().voidTeleport
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));

            if (voidEnhancementData.itemLossChance <= 0) return;

            ArrayList<ItemStack> lostItems = new ArrayList<>();
            for (ItemStack item : event.getPlayer().getInventory().getContents()) {
                if (item == null) continue;

                ItemStack originalItem = item.clone();

                int lostAmount = 0;
                for (int i = 0; i < item.getAmount(); i++) {
                    if (Math.random() * 100 <= voidEnhancementData.itemLossChance) {
                        lostAmount++;
                    }
                }

                if (lostAmount == 0) continue;

                int newAmount = originalItem.getAmount() - lostAmount;
                item.setAmount(newAmount);

                originalItem.setAmount(lostAmount);
                lostItems.add(originalItem);
            }

            IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().addEntry(new LostItems(
                    event.getPlayer().getUniqueId(), lostItems.toArray(new ItemStack[0])));

            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance()
                    .getMessages().voidLostItems
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%items%", lostItems.stream()
                            .map(item -> IridiumSkyblock.getInstance().getMessages().itemsString
                                    .replace("%amount%", String.valueOf(item.getAmount()))
                                    .replace("%item_name%", item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "%type%")
                                    .replace("%type%", item.getType().name().trim().replace("_", " ")))
                            .collect(Collectors.joining(", ")))
            ));
        });
    }
}
