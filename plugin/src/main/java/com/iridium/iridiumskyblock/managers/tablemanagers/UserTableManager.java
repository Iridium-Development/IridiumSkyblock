package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.User;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class UserTableManager extends TableManager<User, Integer> {

    public UserTableManager(ConnectionSource connectionSource, boolean autoCommit) throws SQLException {
        super(connectionSource, User.class, autoCommit);
        sort();
    }

    /**
     * Sort the list of entries by UUID
     */
    public void sort() {
        getEntries().sort(Comparator.comparing(User::getUuid));
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param user The item we are adding
     */
    public void addEntry(User user) {
        getEntries().add(user);
        sort();
    }

    public Optional<User> getUser(UUID uuid) {
        int index = Collections.binarySearch(getEntries(), new User(uuid, ""), Comparator.comparing(User::getUuid));
        if (index < 0) return Optional.empty();
        return Optional.of(getEntries().get(index));
    }
}
