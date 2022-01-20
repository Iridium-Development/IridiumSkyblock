package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandUpgradeTableManager  extends TableManager<IslandUpgrade, Integer> {

    // Comparator.comparing(IslandUpgrade::getIslandId).thenComparing(IslandUpgrade::getUpgrade)

    LinkedHashMap<Integer, List<IslandUpgrade>> islandUpgradeById = new LinkedHashMap<>();

    public IslandUpgradeTableManager(ConnectionSource connectionSource, Class<IslandUpgrade> clazz, Comparator<IslandUpgrade> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandUpgrade> spawnersList = getEntries();
        for (int i = 0, spawnerSize = spawnersList.size(); i < spawnerSize; i++) {
            IslandUpgrade spawners = spawnersList.get(i);
            List<IslandUpgrade> blocks = islandUpgradeById.getOrDefault(spawners.getIslandId(), new ArrayList<>());
            blocks.add(spawners);
            islandUpgradeById.put(spawners.getIslandId(), blocks);
        }

        int valueReward = 0;
        for (List<IslandUpgrade> islandWarps : islandUpgradeById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandUpgrade islandUpgrade) {
        islandUpgrade.setChanged(true);
        List<IslandUpgrade> spawners = islandUpgradeById.getOrDefault(islandUpgrade.getIslandId(), new ArrayList<>());
        spawners.add(islandUpgrade);
        islandUpgradeById.put(islandUpgrade.getIslandId(), spawners);
    }

    @Override
    public void delete(IslandUpgrade islandUpgrade) {
        islandUpgradeById.put(islandUpgrade.getIslandId(), null);
        super.delete(islandUpgrade);
    }

    @Override
    public void clear() {
        islandUpgradeById.clear();
        super.clear();
    }

    public Optional<IslandUpgrade> getEntry(IslandUpgrade islandUpgrade) {
        List<IslandUpgrade> upgrades = islandUpgradeById.get(islandUpgrade.getIslandId());
        if (upgrades == null) return Optional.empty();
        for (IslandUpgrade upgrade : upgrades) {
            if (upgrade.getUpgrade().equalsIgnoreCase(islandUpgrade.getUpgrade())) return Optional.of(upgrade);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandUpgrade>> getIslandUpgradeById() {
        return islandUpgradeById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandUpgrade> getEntries(@NotNull Island island) {
        List<IslandUpgrade> islandUpgrades = islandUpgradeById.getOrDefault(island.getId(), new ArrayList<>());
        return islandUpgrades == null ? new ArrayList<>() : islandUpgrades;
    }
}
