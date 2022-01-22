package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class IslandMissionTableManager extends TableManager<IslandMission, Integer> {

    LinkedHashMap<Integer, List<IslandMission>> islandMissionById = new LinkedHashMap<>();

    public IslandMissionTableManager(ConnectionSource connectionSource, Class<IslandMission> clazz, Comparator<IslandMission> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandMission> islandMissionList = getEntries();
        for (int i = 0, missionSize = islandMissionList.size(); i < missionSize; i++) {
            IslandMission mission = islandMissionList.get(i);
            List<IslandMission> missionList = islandMissionById.getOrDefault(mission.getIslandId(), new ArrayList<>());
            missionList.add(mission);
            islandMissionById.put(mission.getIslandId(), missionList);
        }
    }

    @Override
    public void addEntry(IslandMission islandMission) {
        islandMission.setChanged(true);
        List<IslandMission> islandMissions = islandMissionById.getOrDefault(islandMission.getIslandId(), new ArrayList<>());
        if (islandMissions == null) islandMissions = new ArrayList<>();
        islandMissions.add(islandMission);
        islandMissionById.put(islandMission.getIslandId(), islandMissions);
    }

    @Override
    public void delete(IslandMission islandMission) {
        List<IslandMission> islandMissions = islandMissionById.getOrDefault(islandMission.getIslandId(), new ArrayList<>());
        islandMissions.remove(islandMission);
        islandMissionById.put(islandMission.getIslandId(), islandMissions);
        super.delete(islandMission);
    }

    @Override
    public void clear() {
        islandMissionById.clear();
        super.clear();
    }

    public Optional<IslandMission> getEntry(IslandMission islandMission) {
        List<IslandMission> islandMissions = islandMissionById.getOrDefault(islandMission.getIslandId(), new ArrayList<>());
        for (IslandMission mission : islandMissions) {
            if (mission.getMissionName().equalsIgnoreCase(islandMission.getMissionName())) {
                if (mission.getMissionIndex() == islandMission.getMissionIndex()) return Optional.of(mission);
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandMission>> getIslandMissionById() {
        return islandMissionById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandMission> getEntries(@NotNull Island island) {
        return islandMissionById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public List<IslandMission> getAllEntries() {
        return islandMissionById.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void deleteDataByIsland(Island island) {
        List<IslandMission> islandMissions = islandMissionById.getOrDefault(island.getId(), new ArrayList<>());
        islandMissionById.remove(island.getId());
        super.delete(islandMissions);
    }
}
