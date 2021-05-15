package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MissionsGUI implements GUI {

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
        this.island = island;
        this.missionType = missionType;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory;
        if (missionType == Mission.MissionType.DAILY) {
            inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().dailyMissionGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().dailyMissionGUI.title));

        } else {
            inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().missionsGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().missionsGUI.title));

        }
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().missionsGUI.background);

        if (missionType == Mission.MissionType.DAILY) {
            HashMap<String, Mission> missions = IridiumSkyblock.getInstance().getIslandManager().getDailyIslandMissions(island);
            int i = 0;

            for (String key : missions.keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                List<Placeholder> placeholders = new ArrayList<>();

                for (int j = 1; j <= mission.getMissions().size(); j++) {
                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, j);
                    placeholders.add(new Placeholder("progress_" + j, String.valueOf(islandMission.getProgress())));
                }

                inventory.setItem(IridiumSkyblock.getInstance().getMissions().dailySlots.get(i), ItemStackUtils.makeItem(mission.getItem(), placeholders));
                i++;
            }
        } else {
            AtomicInteger slot = new AtomicInteger(0);
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                if (mission.getMissionType() != Mission.MissionType.ONCE) continue;
                List<Placeholder> placeholders = new ArrayList<>();

                for (int j = 1; j <= mission.getMissions().size(); j++) {
                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, j);
                    placeholders.add(new Placeholder("progress_" + j, String.valueOf(islandMission.getProgress())));
                }

                inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(mission.getItem(), placeholders));
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
