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

    private final LinkedHashMap<UUID, User> userIslandMap = new LinkedHashMap<>();

    public UserTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, User.class, Comparator.comparing(User::getUuid));
        int size = getEntries().size();
        for (int i = 0; i < size; i++) {
            User user = getEntries().get(i);
            userIslandMap.put(user.getUuid(), user);
        }
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param user The item we are adding
     */
    public void addEntry(User user) {
        if (userIslandMap.get(user.getUuid()) != null) {
            System.out.println("Alerte Tentative de Duplication d'entrée !");
            return;
        }
        getEntries().add(user);
        userIslandMap.put(user.getUuid(), user);
    }

    /**
     * Puts the user in userIslandMap
     *
     * @param user The specified User
     */
    public void resortIsland(User user) {
        userIslandMap.replace(user.getUuid(), user);
    }

    public Optional<User> getUser(UUID uuid) {
        User user = userIslandMap.get(uuid);
        if (user == null) return Optional.empty();
        return Optional.of(user);
    }


    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<User> getEntries(@NotNull Island island) {
        List<User> userList = new LinkedList<>();
        for (Map.Entry<UUID, User> entry : userIslandMap.entrySet()) {
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
