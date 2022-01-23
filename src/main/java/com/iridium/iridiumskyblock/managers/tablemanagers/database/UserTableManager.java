package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Add an item to the list
     *
     * @param user The item we are adding
     */
    public void addEntry(User user) {
        if (userIslandMap.get(user.getUuid()) != null) return;
        user.setChanged(true);
        userIslandMap.put(user.getUuid(), user);
        if (IridiumSkyblock.getInstance().getConfiguration().debug) {
            System.out.println("Debugage de la liste/map : \n" +
                    "Map : " + userIslandMap.size());
        }
    }

    /**
     * Puts the user in userIslandMap
     *
     * @param user The specified User
     */
    public void resortIsland(User user) {
        user.setChanged(true);
        userIslandMap.put(user.getUuid(), user);
    }

    public Optional<User> getUser(UUID uuid) {
        User user = userIslandMap.get(uuid);
        if (user == null) return Optional.empty();
        return Optional.of(user);
    }

    public User getUserbyUUID(UUID uuid) {
        return userIslandMap.get(uuid);
    }

    public @Nullable User getUserByUsername(String username) {
        for (User user : userIslandMap.values()) {
            if (user.getName().equalsIgnoreCase(username)) {
                if (username.contains(IridiumSkyblock.getInstance().getConfiguration().bedrockPrefix)) { // Support Bedrock + fixe
                    if (user.getUuid().toString().contains("00000000-0000-0000") == false) continue;
                }
                return user;
            }
        }
        return null;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<User> getEntries(@NotNull Island island) {
        List<User> userList = new ArrayList<>();
        for (User user : userIslandMap.values()) {
            Optional<Island> hasIsland = user.getIsland();
            if (hasIsland.isPresent()) {
                Island islandPlayer = hasIsland.get();
                if (islandPlayer.getId() == island.getId()) userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public void clear() {
        super.clear();
        userIslandMap.clear();
    }

    public LinkedHashMap<UUID, User> getUserIslandMap() {
        return userIslandMap;
    }

}
