package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class IslandLogTableManager extends TableManager<IslandLog, Integer> {

    LinkedHashMap<Integer, List<IslandLog>> islandLogById = new LinkedHashMap<>();

    public IslandLogTableManager(ConnectionSource connectionSource, Class<IslandLog> clazz, Comparator<IslandLog> comparator) throws SQLException {
        super(connectionSource, clazz, comparator);
        List<IslandLog> islandLogsStatic = getEntries();
        for (int i = 0, islandLogsSize = islandLogsStatic.size(); i < islandLogsSize; i++) {
            IslandLog reward = islandLogsStatic.get(i);
            List<IslandLog> rewards = islandLogById.getOrDefault(reward.getIslandId(), new ArrayList<>());
            rewards.add(reward);
            islandLogById.put(reward.getIslandId(), rewards);
        }
    }

    @Override
    public void addEntry(IslandLog islandLog) {
        List<IslandLog> islandLogs = islandLogById.getOrDefault(islandLog.getIslandId(), new ArrayList<>());
        islandLogs.add(islandLog);
        islandLogById.put(islandLog.getIslandId(), islandLogs);
    }

    @Override
    public void delete(IslandLog islandLog) {
        islandLogById.put(islandLog.getIslandId(), null);
        super.delete(islandLog);
    }

    @Override
    public void clear() {
        islandLogById.clear();
        super.clear();
    }

    public LinkedHashMap<Integer, List<IslandLog>> getIslandLogById() {
        return islandLogById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandLog> getEntries(@NotNull Island island) {
        List<IslandLog> islandLogs = islandLogById.getOrDefault(island.getId(), new ArrayList<>());
        return islandLogs == null ? new ArrayList<>() : islandLogs;
    }
}
