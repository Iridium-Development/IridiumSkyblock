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

    private final LinkedHashMap<Integer, User> userIslandMap = new LinkedHashMap<>();

    public UserTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, User.class, Comparator.comparing(User::getUuid));
        int size = getEntries().size();
        for (int i = 0; i < size; i++) {
            userIslandMap.put(i, getEntries().get(i));
        }
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param user The item we are adding
     */
    public void addEntry(User user) {
        for (User user1 : userIslandMap.values()) {
            if (user1.getUuid().equals(user.getUuid())) {
                System.out.println("Alerte Tentative de Duplication d'entrée !");
                return;
            }
        }
        getEntries().add(user);
        userIslandMap.put(userIslandMap.size(), user);
    }

    /**
     * Puts the user in the correct order of userIslandIndex
     *
     * @param user The specified User
     */
    public void resortIsland(User user) {
        for (Map.Entry<Integer, User> userEntry : userIslandMap.entrySet()) {
            if (user.getUuid().equals(userEntry.getValue().getUuid())) {
                userIslandMap.replace(userEntry.getKey(), user);
                return;
            }
        }
    }

    public Optional<User> getUser(UUID uuid) {
        for (Map.Entry<Integer, User> userEntry : userIslandMap.entrySet()) {
            if (uuid.equals(userEntry.getValue().getUuid())) {
                return Optional.of(userEntry.getValue());
            }
        }
        return Optional.empty();
    }


    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<User> getEntries(@NotNull Island island) {
        List<User> userList = new LinkedList<>();
        for (Map.Entry<Integer, User> entry : userIslandMap.entrySet()) {
            User user = entry.getValue();
            Optional<Island> hasIsland = user.getIsland();
            if (hasIsland.isEmpty()) continue;
            Island islandPlayer = hasIsland.get();
            if (islandPlayer.getId() == island.getId()) userList.add(user);
        }
        return userList;
    }

    @Override
    public void clear() {
        super.clear();
        userIslandMap.clear();
    }
}
