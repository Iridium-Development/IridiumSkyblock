package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandReward;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandRewardTableManager extends TableManager<IslandReward, Integer> {

    LinkedHashMap<Integer, List<IslandReward>> islandRewardById = new LinkedHashMap<>();

    public IslandRewardTableManager(ConnectionSource connectionSource, Class<IslandReward> clazz, Comparator<IslandReward> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandReward> rewardList = getEntries();
        for (int i = 0, rewardSize = rewardList.size(); i < rewardSize; i++) {
            IslandReward reward = rewardList.get(i);
            List<IslandReward> rewards = islandRewardById.getOrDefault(reward.getIslandId(), new ArrayList<>());
            rewards.add(reward);
            islandRewardById.put(reward.getIslandId(), rewards);
        }
    }

    @Override
    public void addEntry(IslandReward islandReward) {
        islandReward.setChanged(true);
        List<IslandReward> rewards = islandRewardById.getOrDefault(islandReward.getIslandId(), new ArrayList<>());
        rewards.add(islandReward);
        islandRewardById.put(islandReward.getIslandId(), rewards);
    }

    @Override
    public void delete(IslandReward islandReward) {
        List<IslandReward> rewards = islandRewardById.getOrDefault(islandReward.getIslandId(), new ArrayList<>());
        rewards.remove(islandReward);
        islandRewardById.put(islandReward.getIslandId(), rewards);
        super.delete(islandReward);
    }

    @Override
    public void clear() {
        islandRewardById.clear();
        super.clear();
    }

    public LinkedHashMap<Integer, List<IslandReward>> getIslandRewardById() {
        return islandRewardById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandReward> getEntries(@NotNull Island island) {
        return islandRewardById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public List<IslandReward> deleteDataInHashMap(Island island) {
        List<IslandReward> rewardList = islandRewardById.getOrDefault(island.getId(), new ArrayList<>());
        islandRewardById.remove(island.getId());
        return rewardList;
    }


    @Override
    public void delete(Collection<IslandReward> data) {
        super.delete(data);
    }
}
