package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandPermission;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandPermissionTableManager  extends TableManager<IslandPermission, Integer> {

    LinkedHashMap<Integer, List<IslandPermission>> islandPermissionById = new LinkedHashMap<>();

    // Comparator.comparing(IslandPermission::getIslandId).thenComparing(IslandPermission::getRank).thenComparing(IslandPermission::getPermission)

    public IslandPermissionTableManager(ConnectionSource connectionSource, Class<IslandPermission> clazz, Comparator<IslandPermission> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandPermission> permissionList = getEntries();
        for (int i = 0, rewardSize = permissionList.size(); i < rewardSize; i++) {
            IslandPermission warp = permissionList.get(i);
            List<IslandPermission> warps = islandPermissionById.getOrDefault(warp.getIslandId(), new ArrayList<>());
            warps.add(warp);
            islandPermissionById.put(warp.getIslandId(), warps);
        }

        int valueReward = 0;
        for (List<IslandPermission> islandWarps : islandPermissionById.values()) {
            valueReward = islandWarps.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandPermission islandPermission) {
        islandPermission.setChanged(true);
        List<IslandPermission> permissions = islandPermissionById.getOrDefault(islandPermission.getIslandId(), new ArrayList<>());
        permissions.add(islandPermission);
        islandPermissionById.put(islandPermission.getIslandId(), permissions);
    }

    @Override
    public void delete(IslandPermission islandPermission) {
        islandPermissionById.put(islandPermission.getIslandId(), null);
        super.delete(islandPermission);
    }

    @Override
    public void clear() {
        islandPermissionById.clear();
        super.clear();
    }

    public Optional<IslandPermission> getEntry(IslandPermission islandPermission) {
        List<IslandPermission> permissions = islandPermissionById.get(islandPermission.getIslandId());
        if (permissions == null) return Optional.empty();
        for (IslandPermission permission : permissions) {
            if (permission.getRank().equals(islandPermission.getRank())) {
                if (permission.getPermission().equalsIgnoreCase(islandPermission.getPermission())) return Optional.of(permission);
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandPermission>> getIslandPermissionById() {
        return islandPermissionById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandPermission> getEntries(@NotNull Island island) {
        List<IslandPermission> islandPermissions = islandPermissionById.getOrDefault(island.getId(), new ArrayList<>());
        return islandPermissions == null ? new ArrayList<>() : islandPermissions;
    }
}
