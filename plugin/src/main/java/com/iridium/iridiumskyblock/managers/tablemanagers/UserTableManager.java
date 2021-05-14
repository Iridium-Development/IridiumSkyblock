package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class UserTableManager extends TableManager<User, Integer> {

    // A list of users sorted by island id for binary search
    private final List<User> userIslandIndex;

    public UserTableManager(ConnectionSource connectionSource, boolean autoCommit) throws SQLException {
        super(connectionSource, User.class, autoCommit);
        this.userIslandIndex = new ArrayList<>(getEntries());
        sort();
    }

    /**
     * Sort the list of entries by UUID
     */
    public void sort() {
        getEntries().sort(Comparator.comparing(User::getUuid));
        userIslandIndex.sort(Comparator.comparing(user -> user.getIsland().map(Island::getId).orElse(0)));
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param user The item we are adding
     */
    public void addEntry(User user) {
        int index = Collections.binarySearch(getEntries(), user, Comparator.comparing(User::getUuid));
        getEntries().add(index < 0 ? -(index + 1) : index, user);

        int islandIndex = Collections.binarySearch(userIslandIndex, user, Comparator.comparing(u -> u.getIsland().map(Island::getId).orElse(0)));
        userIslandIndex.add(islandIndex < 0 ? -(islandIndex + 1) : islandIndex, user);
    }

    /**
     * Puts the user in the correct order of userIslandIndex
     *
     * @param user The specified User
     */
    public void resortIsland(User user) {
        userIslandIndex.remove(user);
        int islandIndex = Collections.binarySearch(userIslandIndex, user, Comparator.comparing(u -> u.getIsland().map(Island::getId).orElse(0)));
        userIslandIndex.add(islandIndex < 0 ? -(islandIndex + 1) : islandIndex, user);
    }

    public Optional<User> getUser(UUID uuid) {
        int index = Collections.binarySearch(getEntries(), new User(uuid, ""), Comparator.comparing(User::getUuid));
        if (index < 0) return Optional.empty();
        return Optional.of(getEntries().get(index));
    }


    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<User> getEntries(@NotNull Island island) {
        int index = getIndex(island);
        if (index == -1) return Collections.emptyList();
        int currentIndex = index - 1;
        List<User> result = new ArrayList<>();
        result.add(userIslandIndex.get(index));

        while (true) {
            if (currentIndex < 0) break;
            User user = userIslandIndex.get(currentIndex);
            if (island.equals(user.getIsland().orElse(null))) {
                result.add(userIslandIndex.get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index + 1;

        while (true) {
            if (currentIndex >= userIslandIndex.size()) break;
            User user = userIslandIndex.get(currentIndex);
            if (island.equals(user.getIsland().orElse(null))) {
                result.add(userIslandIndex.get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Gets the index of the island -1 if not found
     *
     * @param island The specified island
     * @return The index where its located
     */
    private int getIndex(@NotNull Island island) {
        int first = 0;
        int last = userIslandIndex.size() - 1;
        int mid = last / 2;
        while (first <= last) {
            User user = userIslandIndex.get(mid);
            int islandId = user.getIsland().isPresent() ? user.getIsland().get().getId() : 0;
            if (islandId < island.getId()) {
                first = mid + 1;
            } else if (islandId == island.getId()) {
                return mid;
            } else {
                last = mid - 1;
            }
            mid = (first + last) / 2;
        }
        return -1;
    }
}
