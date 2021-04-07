package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
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

public class MissionsGUI implements GUI {

    private final Island island;
    private final Mission.MissionType missionType;

    public MissionsGUI(@NotNull Island island, @NotNull Mission.MissionType missionType) {
        this.island = island;
        this.missionType = missionType;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, missionType == Mission.MissionType.ONCE ? IridiumSkyblock.getInstance().getInventories().missionsGUISize : 27, StringUtils.color("&7Island Missions"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().filler));
        }
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
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                if (mission.getMissionType() != Mission.MissionType.ONCE) continue;
                List<Placeholder> placeholders = new ArrayList<>();
                for (int j = 1; j <= mission.getMissions().size(); j++) {
                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, j);
                    placeholders.add(new Placeholder("progress_" + j, String.valueOf(islandMission.getProgress())));
                    IridiumSkyblock.getInstance().getLogger().info(j + " - " + islandMission.getProgress());
                }
                inventory.setItem(mission.getItem().slot, ItemStackUtils.makeItem(mission.getItem(), placeholders));
            }
        }
        return inventory;
    }
}
