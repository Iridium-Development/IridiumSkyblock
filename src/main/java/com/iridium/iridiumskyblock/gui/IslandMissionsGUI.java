package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IslandMissionsGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandMissionsGUI(@NotNull Island island, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().missionsGUI, previousInventory, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().missionsGUI.background);

        AtomicInteger slot = new AtomicInteger(0);
        for (Map.Entry<String, Mission> entry : IridiumSkyblock.getInstance().getMissionsList().entrySet()) {
            if (entry.getValue().getMissionType() != Mission.MissionType.ONCE) continue;

            List<Placeholder> placeholders = IntStream.range(0, entry.getValue().getMissions().size())
                    .boxed()
                    .map(integer -> IridiumSkyblock.getInstance().getIslandManager().getIslandMission(getIsland(), entry.getValue(), entry.getKey(), integer))
                    .map(islandMission -> new Placeholder("progress_"+(islandMission.getMissionIndex()+1), String.valueOf(islandMission.getProgress())))
                    .collect(Collectors.toList());

            inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(entry.getValue().getItem(), placeholders));
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
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
        // Do nothing here
    }

}
