package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.SortedList;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumteams.database.TeamData;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ForeignIslandTableManager<T extends TeamData, S> extends TableManager<T, S> {

    private final Comparator<T> comparator;
    private final SortedList<T> islandSortedList;

    public ForeignIslandTableManager(ConnectionSource connectionSource, Class<T> clazz, Comparator<T> comparator) throws SQLException {
        super(connectionSource, clazz, comparator);
        this.comparator = comparator;
        this.islandSortedList = new SortedList<>(Comparator.comparing(TeamData::getTeamID));
        this.islandSortedList.addAll(getEntries());
        sort();
    }

    @Override
    public CompletableFuture<Void> delete(T t) {
        islandSortedList.remove(t);
        return super.delete(t);
    }

    @Override
    public CompletableFuture<Void> delete(Collection<T> t) {
        islandSortedList.removeAll(t);
        return super.delete(t);
    }

    @Override
    public void addEntry(T t) {
        super.addEntry(t);
        islandSortedList.add(t);
    }

    public List<T> getEntries(@NotNull Island island) {
        int index = Collections.binarySearch(islandSortedList, new TeamData(island), Comparator.comparing(TeamData::getTeamID));
        if (index < 0) return Collections.emptyList();

        int currentIndex = index - 1;
        List<T> result = new ArrayList<>();
        result.add(getEntries().get(index));

        while (true) {
            if (currentIndex < 0) break;
            TeamData islandData = getEntries().get(currentIndex);
            if (islandData == null) {
                currentIndex--;
                continue;
            }
            if (island.getId() == islandData.getTeamID()) {
                result.add(getEntries().get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index + 1;

        while (true) {
            if (currentIndex >= getEntries().size()) break;
            TeamData islandData = getEntries().get(currentIndex);
            if (islandData == null) {
                currentIndex++;
                continue;
            }
            if (island.getId() == islandData.getTeamID()) {
                result.add(getEntries().get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    public void sort() {
        getEntries().sort(comparator);
        islandSortedList.sort(Comparator.comparing(TeamData::getTeamID));
    }
}
