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

import java.util.Collections;
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
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        if (missionType == Mission.MissionType.DAILY) {
            List<Mission> missionList = IridiumSkyblock.getInstance().getIslandManager().getDailyIslandMissions(island);
            for (int i = 0; i < IridiumSkyblock.getInstance().getMissions().dailySlots.size(); i++) {
                IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, missionList.get(i));
                inventory.setItem(IridiumSkyblock.getInstance().getMissions().dailySlots.get(i), ItemStackUtils.makeItem(missionList.get(i).getItem(), Collections.singletonList(new Placeholder("progress", String.valueOf(islandMission.getProgress())))));
            }
        } else {
            IridiumSkyblock.getInstance().getMissionsList().stream().filter(mission -> mission.getMissionType() == missionType).forEach(mission -> {
                IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission);
                inventory.setItem(mission.getItem().slot, ItemStackUtils.makeItem(mission.getItem(), Collections.singletonList(new Placeholder("progress", String.valueOf(islandMission.getProgress())))));
            });
        }
        return inventory;
    }
}
