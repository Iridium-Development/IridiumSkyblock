package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapter;
import com.iridium.iridiumteams.database.IridiumUser;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

public class UserTableManager extends TableManager<UUID, User, Integer> {

    public UserTableManager(DatabaseAdapter<User, Integer> databaseAdapter) throws SQLException {
        super(IridiumUser::getUuid, databaseAdapter);
    }

    public Optional<User> getUser(UUID uuid) {
        return super.getEntry(uuid);
    }
}
