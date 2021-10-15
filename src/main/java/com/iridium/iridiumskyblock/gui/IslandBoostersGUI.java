package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;

/**
 * GUI which allows users to manage the Island Boosters.
 */
public class IslandBoostersGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandBoostersGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().boostersGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        for (Map.Entry<String, Booster> entry : IridiumSkyblock.getInstance().getBoosterList().entrySet()) {
            Item item = entry.getValue().item;
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(getIsland(), entry.getKey());
            long minutes = LocalDateTime.now().until(islandBooster.getTime(), ChronoUnit.MINUTES);
            long seconds = LocalDateTime.now().until(islandBooster.getTime(), ChronoUnit.SECONDS) - minutes * 60;
            inventory.setItem(item.slot, ItemStackUtils.makeItem(item, Arrays.asList(
                    new Placeholder("timeremaining_minutes", String.valueOf(Math.max(minutes, 0))),
                    new Placeholder("timeremaining_seconds", String.valueOf(Math.max(seconds, 0))),
                    new Placeholder("crystalcost", IridiumSkyblock.getInstance().getNumberFormatter().format(entry.getValue().crystalsCost)),
                    new Placeholder("vaultcost", IridiumSkyblock.getInstance().getNumberFormatter().format(entry.getValue().vaultCost))
            )));
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        for (Map.Entry<String, Booster> entry : IridiumSkyblock.getInstance().getBoosterList().entrySet()) {
            if (entry.getValue().item.slot == event.getSlot()) {
                String command = IridiumSkyblock.getInstance().getCommands().boostersCommand.aliases.get(0);
                Bukkit.dispatchCommand(player, "is " + command + " " + entry.getKey());
                return;
            }
        }
    }
}
