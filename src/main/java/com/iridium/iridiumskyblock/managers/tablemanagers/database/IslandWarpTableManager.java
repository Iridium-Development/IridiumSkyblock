package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class IslandWarpTableManager extends TableManager<IslandWarp, Integer> {

    LinkedHashMap<Integer, List<IslandWarp>> islandWarpById = new LinkedHashMap<>();

    public IslandWarpTableManager(ConnectionSource connectionSource, Class<IslandWarp> clazz, Comparator<IslandWarp> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandWarp> warpsList = getEntries();
        for (int i = 0, rewardSize = warpsList.size(); i < rewardSize; i++) {
            IslandWarp warp = warpsList.get(i);
            List<IslandWarp> warps = islandWarpById.getOrDefault(warp.getIslandId(), new ArrayList<>());
            warps.add(warp);
            islandWarpById.put(warp.getIslandId(), warps);
        }

        int valueReward = 0;
        for (List<IslandWarp> islandWarps : islandWarpById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandWarp islandWarp) {
        islandWarp.setChanged(true);
        List<IslandWarp> warps = islandWarpById.getOrDefault(islandWarp.getIslandId(), new ArrayList<>());
        if (warps == null) warps = new ArrayList<>();
        warps.add(islandWarp);
        islandWarpById.put(islandWarp.getIslandId(), warps);
    }

    @Override
    public void delete(IslandWarp islandWarp) {
        islandWarpById.put(islandWarp.getIslandId(), null);
        super.delete(islandWarp);
    }

    @Override
    public void clear() {
        islandWarpById.clear();
        super.clear();
    }

    public LinkedHashMap<Integer, List<IslandWarp>> getIslandWarpById() {
        return islandWarpById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandWarp> getEntries(@NotNull Island island) {
        List<IslandWarp> warpList = islandWarpById.getOrDefault(island.getId(), new ArrayList<>());
        return warpList == null ? new ArrayList<>() : warpList;
    }
}
