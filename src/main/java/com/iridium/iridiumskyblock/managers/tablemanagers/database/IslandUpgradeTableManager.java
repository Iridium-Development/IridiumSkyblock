package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandUpgradeTableManager  extends TableManager<IslandUpgrade, Integer> {

    LinkedHashMap<Integer, List<IslandUpgrade>> islandUpgradeById = new LinkedHashMap<>();

    public IslandUpgradeTableManager(ConnectionSource connectionSource, Class<IslandUpgrade> clazz, Comparator<IslandUpgrade> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandUpgrade> upgradeList = getEntries();
        for (int i = 0, upgradeSize = upgradeList.size(); i < upgradeSize; i++) {
            IslandUpgrade upgrade = upgradeList.get(i);
            List<IslandUpgrade> islandUpgrades = islandUpgradeById.getOrDefault(upgrade.getIslandId(), new ArrayList<>());
            islandUpgrades.add(upgrade);
            islandUpgradeById.put(upgrade.getIslandId(), islandUpgrades);
        }
    }

    @Override
    public void addEntry(IslandUpgrade islandUpgrade) {
        islandUpgrade.setChanged(true);
        List<IslandUpgrade> upgrades = islandUpgradeById.getOrDefault(islandUpgrade.getIslandId(), new ArrayList<>());
        if (upgrades == null) upgrades = new ArrayList<>();
        upgrades.add(islandUpgrade);
        islandUpgradeById.put(islandUpgrade.getIslandId(), upgrades);
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
        if (upgrades == null || upgrades.isEmpty()) return Optional.empty();
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
