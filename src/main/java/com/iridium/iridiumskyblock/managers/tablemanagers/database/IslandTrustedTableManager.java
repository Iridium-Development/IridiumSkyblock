package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandTrustedTableManager extends TableManager<IslandTrusted, Integer> {

    LinkedHashMap<Integer, List<IslandTrusted>> islandTrustedById = new LinkedHashMap<>();

    public IslandTrustedTableManager(ConnectionSource connectionSource, Class<IslandTrusted> clazz, Comparator<IslandTrusted> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandTrusted> trustedList = getEntries();
        for (int i = 0, trustedSize = trustedList.size(); i < trustedSize; i++) {
            IslandTrusted islandTrusted = trustedList.get(i);
            List<IslandTrusted> trusteds = islandTrustedById.getOrDefault(islandTrusted.getIslandId(), new ArrayList<>());
            trusteds.add(islandTrusted);
            islandTrustedById.put(islandTrusted.getIslandId(), trusteds);
        }
    }

    @Override
    public void addEntry(IslandTrusted islandTrusted) {
        islandTrusted.setChanged(true);
        List<IslandTrusted> trusts = islandTrustedById.getOrDefault(islandTrusted.getIslandId(), new ArrayList<>());
        if (trusts == null) trusts = new ArrayList<>();
        trusts.add(islandTrusted);
        islandTrustedById.put(islandTrusted.getIslandId(), trusts);
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
