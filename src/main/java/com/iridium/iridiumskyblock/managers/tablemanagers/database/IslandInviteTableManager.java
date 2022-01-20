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
        List<IslandInvite> warpsList = getEntries();
        for (int i = 0, rewardSize = warpsList.size(); i < rewardSize; i++) {
            IslandInvite warp = warpsList.get(i);
            List<IslandInvite> warps = islandInviteById.getOrDefault(warp.getIslandId(), new ArrayList<>());
            warps.add(warp);
            islandInviteById.put(warp.getIslandId(), warps);
        }

        int valueReward = 0;
        for (List<IslandInvite> islandWarps : islandInviteById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
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
        islandInviteById.put(islandInvite.getIslandId(), null);
        super.delete(islandInvite);
    }

    @Override
    public void clear() {
        islandInviteById.clear();
        super.clear();
    }

    public Optional<IslandInvite> getEntry(IslandInvite islandInvite) {
        List<IslandInvite> invites = islandInviteById.get(islandInvite.getIslandId());
        if (invites == null) return Optional.empty();
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
        List<IslandInvite> islandInvites = islandInviteById.getOrDefault(island.getId(), new ArrayList<>());
        return islandInvites == null ? new ArrayList<>() : islandInvites;
    }
}
