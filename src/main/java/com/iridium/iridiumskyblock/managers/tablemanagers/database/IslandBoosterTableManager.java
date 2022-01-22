package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBoosterTableManager extends TableManager<IslandBooster, Integer> {

    LinkedHashMap<Integer, List<IslandBooster>> islandBoosterById = new LinkedHashMap<>();

    public IslandBoosterTableManager(ConnectionSource connectionSource, Class<IslandBooster> clazz, Comparator<IslandBooster> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBooster> boosterList = getEntries();
        for (int i = 0, boostSize = boosterList.size(); i < boostSize; i++) {
            IslandBooster booster = boosterList.get(i);
            List<IslandBooster> blocks = islandBoosterById.getOrDefault(booster.getIslandId(), new ArrayList<>());
            blocks.add(booster);
            islandBoosterById.put(booster.getIslandId(), blocks);
        }
    }

    @Override
    public void addEntry(IslandBooster islandBooster) {
        islandBooster.setChanged(true);
        List<IslandBooster> boosters = islandBoosterById.getOrDefault(islandBooster.getIslandId(), new ArrayList<>());
        boosters.add(islandBooster);
        islandBoosterById.put(islandBooster.getIslandId(), boosters);
    }

    @Override
    public void delete(IslandBooster islandBooster) {
        List<IslandBooster> boosters = islandBoosterById.getOrDefault(islandBooster.getIslandId(), new ArrayList<>());
        boosters.remove(islandBooster);
        islandBoosterById.put(islandBooster.getIslandId(), boosters);
        super.delete(islandBooster);
    }

    @Override
    public void clear() {
        islandBoosterById.clear();
        super.clear();
    }

    public Optional<IslandBooster> getEntry(IslandBooster islandBooster) {
        List<IslandBooster> boosters = islandBoosterById.getOrDefault(islandBooster.getIslandId(), new ArrayList<>());
        for (IslandBooster booster : boosters) {
            if (booster.getBooster().equalsIgnoreCase(islandBooster.getBooster())) return Optional.of(booster);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandBooster>> getIslandBoosterById() {
        return islandBoosterById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandBooster> getEntries(@NotNull Island island) {
        return islandBoosterById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public void deleteDataByIsland(Island island) {
        List<IslandBooster> islandBoosters = islandBoosterById.getOrDefault(island.getId(), new ArrayList<>());
        islandBoosterById.remove(island.getId());
        super.delete(islandBoosters);
    }
}
