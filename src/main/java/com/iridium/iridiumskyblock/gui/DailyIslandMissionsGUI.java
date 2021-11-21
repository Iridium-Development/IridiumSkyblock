package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DailyIslandMissionsGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public DailyIslandMissionsGUI(@NotNull Island island, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().dailyMissionGUI, previousInventory, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().missionsGUI.background);

        Map<String, Mission> missions = IridiumSkyblock.getInstance().getIslandManager().getDailyIslandMissions(getIsland());
        int i = 0;

        for (Map.Entry<String, Mission> entry : missions.entrySet()) {
            List<Placeholder> placeholders = IntStream.range(0, entry.getValue().getMissions().size())
                    .boxed()
                    .map(integer -> IridiumSkyblock.getInstance().getIslandManager().getIslandMission(getIsland(), entry.getValue(), entry.getKey(), integer))
                    .map(islandMission -> new Placeholder("progress_"+(islandMission.getMissionIndex()+1), String.valueOf(islandMission.getProgress())))
                    .collect(Collectors.toList());

            if (IridiumSkyblock.getInstance().getMissions().dailySlots.size() > i) {
                Integer slot = IridiumSkyblock.getInstance().getMissions().dailySlots.get(i);
                inventory.setItem(slot, ItemStackUtils.makeItem(entry.getValue().getItem(), placeholders));
            }
            i++;
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
