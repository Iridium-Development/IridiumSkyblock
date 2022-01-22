package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSpawners;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandSpawnersTableManager extends TableManager<IslandSpawners, Integer> {

    LinkedHashMap<Integer, List<IslandSpawners>> islandSpawnerById = new LinkedHashMap<>();

    public IslandSpawnersTableManager(ConnectionSource connectionSource, Class<IslandSpawners> clazz, Comparator<IslandSpawners> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandSpawners> spawnersList = getEntries();
        for (int i = 0, spawnerSize = spawnersList.size(); i < spawnerSize; i++) {
            IslandSpawners spawners = spawnersList.get(i);
            List<IslandSpawners> spawner = islandSpawnerById.getOrDefault(spawners.getIslandId(), new ArrayList<>());
            spawner.add(spawners);
            islandSpawnerById.put(spawners.getIslandId(), spawner);
        }
    }

    @Override
    public void addEntry(IslandSpawners islandSpawners) {
        islandSpawners.setChanged(true);
        List<IslandSpawners> spawners = islandSpawnerById.getOrDefault(islandSpawners.getIslandId(), new ArrayList<>());
        spawners.add(islandSpawners);
        islandSpawnerById.put(islandSpawners.getIslandId(), spawners);
    }

    @Override
    public void delete(IslandSpawners islandSpawners) {
        List<IslandSpawners> spawners = islandSpawnerById.getOrDefault(islandSpawners.getIslandId(), new ArrayList<>());
        spawners.remove(islandSpawners);
        islandSpawnerById.put(islandSpawners.getIslandId(), spawners);
        super.delete(islandSpawners);
    }

    @Override
    public void clear() {
        islandSpawnerById.clear();
        super.clear();
    }

    public Optional<IslandSpawners> getEntry(IslandSpawners islandSpawners) {
        List<IslandSpawners> spawners = islandSpawnerById.getOrDefault(islandSpawners.getIslandId(), new ArrayList<>());
        for (IslandSpawners spawner : spawners) {
            if (islandSpawners.getSpawnerType().equals(spawner.getSpawnerType())) return Optional.of(spawner);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandSpawners>> getIslandSpawnerById() {
        return islandSpawnerById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandSpawners> getEntries(@NotNull Island island) {
        return islandSpawnerById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public void deleteDataByIsland(Island island) {
        List<IslandSpawners> spawnersList = islandSpawnerById.getOrDefault(island.getId(), new ArrayList<>());
        islandSpawnerById.remove(island.getId());
        super.delete(spawnersList);
    }
}
