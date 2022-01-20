package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBoosterTableManager extends TableManager<IslandBooster, Integer> {

    // Comparator.comparing(IslandBooster::getIslandId).thenComparing(IslandBooster::getBooster)

    LinkedHashMap<Integer, List<IslandBooster>> islandBoosterById = new LinkedHashMap<>();

    public IslandBoosterTableManager(ConnectionSource connectionSource, Class<IslandBooster> clazz, Comparator<IslandBooster> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBooster> blocksList = getEntries();
        for (int i = 0, rewardSize = blocksList.size(); i < rewardSize; i++) {
            IslandBooster warp = blocksList.get(i);
            List<IslandBooster> blocks = islandBoosterById.getOrDefault(warp.getIslandId(), new ArrayList<>());
            blocks.add(warp);
            islandBoosterById.put(warp.getIslandId(), blocks);
        }

        int valueReward = 0;
        for (List<IslandBooster> islandWarps : islandBoosterById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandBooster islandBooster) {
        islandBooster.setChanged(true);
        List<IslandBooster> boosters = islandBoosterById.getOrDefault(islandBooster.getIslandId(), new ArrayList<>());
        if (boosters == null) boosters = new ArrayList<>();
        boosters.add(islandBooster);
        islandBoosterById.put(islandBooster.getIslandId(), boosters);
    }

    @Override
    public void delete(IslandBooster islandBooster) {
        islandBoosterById.put(islandBooster.getIslandId(), null);
        super.delete(islandBooster);
    }

    @Override
    public void clear() {
        islandBoosterById.clear();
        super.clear();
    }

    public Optional<IslandBooster> getEntry(IslandBooster islandBlocks) {
        List<IslandBooster> blocks = islandBoosterById.get(islandBlocks.getIslandId());
        if (blocks == null) return Optional.empty();
        for (IslandBooster block : blocks) {
            if (block.getBooster().equalsIgnoreCase(islandBlocks.getBooster())) return Optional.of(islandBlocks);
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
        List<IslandBooster> islandBoosters = islandBoosterById.getOrDefault(island.getId(), new ArrayList<>());
        return islandBoosters == null ? new ArrayList<>() : islandBoosters;
    }
}
