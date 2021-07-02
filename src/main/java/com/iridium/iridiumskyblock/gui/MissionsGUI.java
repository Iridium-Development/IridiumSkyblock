package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MissionsGUI extends GUI {

    private final Island island;
    private final Mission.MissionType missionType;

    /**
     * The default constructor.
     *
     * @param island      The Island this GUI belongs to
     * @param missionType The amount of times the containing missions can be claimed
     * @see com.iridium.iridiumskyblock.Mission.MissionType
     */
    public MissionsGUI(@NotNull Island island, @NotNull Mission.MissionType missionType) {
        super(missionType == Mission.MissionType.DAILY ? IridiumSkyblock.getInstance().getInventories().dailyMissionGUI : IridiumSkyblock.getInstance().getInventories().missionsGUI, island);
        this.island = island;
        this.missionType = missionType;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().missionsGUI.background);

        if (missionType == Mission.MissionType.DAILY) {
            HashMap<String, Mission> missions = IridiumSkyblock.getInstance().getIslandManager().getDailyIslandMissions(island);
            int i = 0;

            for (Map.Entry<String, Mission> entry : missions.entrySet()) {
                List<Placeholder> placeholders = new ArrayList<>();

                for (int j = 1; j <= entry.getValue().getMissions().size(); j++) {
                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, entry.getValue(), entry.getKey(), j);
                    placeholders.add(new Placeholder("progress_" + j, String.valueOf(islandMission.getProgress())));
                }
                if (IridiumSkyblock.getInstance().getMissions().dailySlots.size() > i) {
                    Integer slot = IridiumSkyblock.getInstance().getMissions().dailySlots.get(i);
                    inventory.setItem(slot, ItemStackUtils.makeItem(entry.getValue().getItem(), placeholders));
                }
                i++;
            }
        } else {
            AtomicInteger slot = new AtomicInteger(0);
            for (Map.Entry<String, Mission> entry : IridiumSkyblock.getInstance().getMissionsList().entrySet()) {
                if (entry.getValue().getMissionType() != Mission.MissionType.ONCE) continue;
                List<Placeholder> placeholders = new ArrayList<>();

                for (int j = 1; j <= entry.getValue().getMissions().size(); j++) {
                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, entry.getValue(), entry.getKey(), j);
                    placeholders.add(new Placeholder("progress_" + j, String.valueOf(islandMission.getProgress())));
                }

                inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(entry.getValue().getItem(), placeholders));
            }
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
