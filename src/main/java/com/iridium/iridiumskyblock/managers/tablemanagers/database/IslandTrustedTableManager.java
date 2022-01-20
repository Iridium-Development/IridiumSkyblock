package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandTrustedTableManager extends TableManager<IslandTrusted, Integer> {

    // Comparator.comparing(IslandTrusted::getIslandId).thenComparing(islandTrusted -> islandTrusted.getUser().getUuid())

    LinkedHashMap<Integer, List<IslandTrusted>> islandTrustedById = new LinkedHashMap<>();

    public IslandTrustedTableManager(ConnectionSource connectionSource, Class<IslandTrusted> clazz, Comparator<IslandTrusted> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandTrusted> spawnersList = getEntries();
        for (int i = 0, spawnerSize = spawnersList.size(); i < spawnerSize; i++) {
            IslandTrusted spawners = spawnersList.get(i);
            List<IslandTrusted> blocks = islandTrustedById.getOrDefault(spawners.getIslandId(), new ArrayList<>());
            blocks.add(spawners);
            islandTrustedById.put(spawners.getIslandId(), blocks);
        }

        int valueReward = 0;
        for (List<IslandTrusted> islandWarps : islandTrustedById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandTrusted islandTrusted) {
        islandTrusted.setChanged(true);
        List<IslandTrusted> spawners = islandTrustedById.getOrDefault(islandTrusted.getIslandId(), new ArrayList<>());
        spawners.add(islandTrusted);
        islandTrustedById.put(islandTrusted.getIslandId(), spawners);
    }

    @Override
    public void delete(IslandTrusted islandTrusted) {
        islandTrustedById.put(islandTrusted.getIslandId(), null);
        super.delete(islandTrusted);
    }

    @Override
    public void clear() {
        islandTrustedById.clear();
        super.clear();
    }

    public Optional<IslandTrusted> getEntry(IslandTrusted islandTrusted) {
        List<IslandTrusted> spawners = islandTrustedById.get(islandTrusted.getIslandId());
        if (spawners == null) return Optional.empty();
        for (IslandTrusted spawner : spawners) {
            if (spawner.getUser().getUuid().equals(islandTrusted.getUser().getUuid())) return Optional.of(spawner);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandTrusted>> getIslandTrustedById() {
        return islandTrustedById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandTrusted> getEntries(@NotNull Island island) {
        List<IslandTrusted> islandTrusted = islandTrustedById.getOrDefault(island.getId(), new ArrayList<>());
        return islandTrusted == null ? new ArrayList<>() : islandTrusted;
    }
}
