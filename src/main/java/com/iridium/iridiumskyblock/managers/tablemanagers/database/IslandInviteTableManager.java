package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandInviteTableManager extends TableManager<IslandInvite, Integer> {

    LinkedHashMap<Integer, List<IslandInvite>> islandInviteById = new LinkedHashMap<>();

    public IslandInviteTableManager(ConnectionSource connectionSource, Class<IslandInvite> clazz, Comparator<IslandInvite> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandInvite> inviteList = getEntries();
        for (int i = 0, inviteSize = inviteList.size(); i < inviteSize; i++) {
            IslandInvite invite = inviteList.get(i);
            List<IslandInvite> invites = islandInviteById.getOrDefault(invite.getIslandId(), new ArrayList<>());
            invites.add(invite);
            islandInviteById.put(invite.getIslandId(), invites);
        }
    }

    @Override
    public void addEntry(IslandInvite islandInvite) {
        islandInvite.setChanged(true);
        List<IslandInvite> invites = islandInviteById.getOrDefault(islandInvite.getIslandId(), new ArrayList<>());
        invites.add(islandInvite);
        islandInviteById.put(islandInvite.getIslandId(), invites);
    }

    @Override
    public void delete(IslandInvite islandInvite) {
        List<IslandInvite> invites = islandInviteById.getOrDefault(islandInvite.getIslandId(), new ArrayList<>());
        invites.remove(islandInvite);
        islandInviteById.put(islandInvite.getIslandId(), invites);
        super.delete(islandInvite);
    }

    @Override
    public void clear() {
        islandInviteById.clear();
        super.clear();
    }

    public Optional<IslandInvite> getEntry(IslandInvite islandInvite) {
        List<IslandInvite> invites = islandInviteById.getOrDefault(islandInvite.getIslandId(), new ArrayList<>());
        for (IslandInvite invite : invites) {
            if (islandInvite.getUser().getUuid().equals(invite.getUser().getUuid())) return Optional.of(invite);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandInvite>> getIslandInviteById() {
        return islandInviteById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandInvite> getEntries(@NotNull Island island) {
        return islandInviteById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public List<IslandInvite> deleteDataInHashMap(Island island) {
        List<IslandInvite> islandInvites = islandInviteById.getOrDefault(island.getId(), new ArrayList<>());
        islandInviteById.remove(island.getId());
        return islandInvites;
    }


    @Override
    public void delete(Collection<IslandInvite> data) {
        super.delete(data);
    }
}
