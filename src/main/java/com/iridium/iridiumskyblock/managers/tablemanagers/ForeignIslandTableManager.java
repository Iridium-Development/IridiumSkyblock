package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandData;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 *
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class ForeignIslandTableManager<T extends IslandData, S> extends TableManager<T, S> {

    private final Comparator<T> comparator;

    public ForeignIslandTableManager(ConnectionSource connectionSource, Class<T> clazz, Comparator<T> comparator) throws SQLException {
        super(connectionSource, clazz, comparator);
        this.comparator = comparator;
        sort();
    }

    @Override
    public void addEntry(T t) {
        getEntries().add(t);
    }

    /**
     * Sort the list of entries by island id
     */
    private void sort() {
        getEntries().sort(comparator);
    }

    public Optional<T> getEntry(T t) {
        int index = Collections.binarySearch(getEntries(), t, comparator);
        if (index < 0) return Optional.empty();
        return Optional.ofNullable(getEntries().get(index));
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<T> getEntries(@NotNull Island island) {
        int index = Collections.binarySearch(getEntries(), new IslandData(island), Comparator.comparing(IslandData::getIslandID));
        if (index < 0) return Collections.emptyList();

        int currentIndex = index - 1;
        List<T> result = new ArrayList<>();
        result.add(getEntries().get(index));

        while (true) {
            if (currentIndex < 0) break;
            IslandData islandData = getEntries().get(currentIndex);
            if (islandData == null) {
                currentIndex--;
                continue;
            }
            if (island.getId()== islandData.getIslandID()){
                result.add(getEntries().get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index + 1;

        while (true) {
            if (currentIndex >= getEntries().size()) break;
            IslandData islandData = getEntries().get(currentIndex);
            if (islandData == null) {
                currentIndex++;
                continue;
            }
            if (island.getId()== islandData.getIslandID()){
                result.add(getEntries().get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    public void deleteDuplicates() {
        List<String> islandDataList = new ArrayList<>();
        List<T> remove = new ArrayList<>();
        for (T islandData : getEntries()) {
            Optional<Island> island = islandData.getIsland();
            if (island.isPresent()) {
                if (islandDataList.contains(islandData.getUniqueKey())) {
                    remove.add(islandData);
                } else {
                    islandDataList.add(islandData.getUniqueKey());
                }
            } else {
                remove.add(islandData);
            }
        }
        delete(remove);
    }
}
