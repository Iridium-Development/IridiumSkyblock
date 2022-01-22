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

    public IslandPermissionTableManager(ConnectionSource connectionSource, Class<IslandPermission> clazz, Comparator<IslandPermission> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandPermission> permissionList = getEntries();
        for (int i = 0, permissionSize = permissionList.size(); i < permissionSize; i++) {
            IslandPermission permission = permissionList.get(i);
            List<IslandPermission> permissions = islandPermissionById.getOrDefault(permission.getIslandId(), new ArrayList<>());
            permissions.add(permission);
            islandPermissionById.put(permission.getIslandId(), permissions);
        }
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
        List<IslandPermission> permissions = islandPermissionById.getOrDefault(islandPermission.getIslandId(), new ArrayList<>());
        permissions.remove(islandPermission);
        islandPermissionById.put(islandPermission.getIslandId(), permissions);
        super.delete(islandPermission);
    }

    @Override
    public void clear() {
        islandPermissionById.clear();
        super.clear();
    }

    public Optional<IslandPermission> getEntry(IslandPermission islandPermission) {
        List<IslandPermission> permissions = islandPermissionById.getOrDefault(islandPermission.getIslandId(), new ArrayList<>());
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
        return islandPermissionById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public void deleteDataByIsland(Island island) {
        List<IslandPermission> islandPermissions = islandPermissionById.getOrDefault(island.getId(), new ArrayList<>());
        islandPermissionById.remove(island.getId());
        super.delete(islandPermissions);
    }
}
