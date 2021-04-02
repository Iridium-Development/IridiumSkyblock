package com.iridium.iridiumskyblock.gui;

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
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().missionsGUISize, StringUtils.color("&7Island Missions"));
        IridiumSkyblock.getInstance().getMissionsList().stream().filter(mission -> mission.getMissionType() == missionType).forEach(mission -> {
            IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission);
            inventory.setItem(mission.getItem().slot, ItemStackUtils.makeItem(mission.getItem(), Collections.singletonList(new Placeholder("progress", String.valueOf(islandMission.getProgress())))));
        });
        return inventory;
    }
}
