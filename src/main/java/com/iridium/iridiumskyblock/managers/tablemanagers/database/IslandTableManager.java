package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class IslandTableManager extends TableManager<Island, Integer> {

    LinkedHashMap<Integer, Island> islandMapByID = new LinkedHashMap<>();

    public IslandTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Island.class, Comparator.comparing(Island::getId));
        IridiumSkyblock.getInstance().getLogger().info("Nouveau Systeme des îles : ok");
        List<Island> islandsStatic = getEntries();
        for (int index = 0, sizeEntries = islandsStatic.size(); index < sizeEntries; index++) {
            Island island = islandsStatic.get(index);
            islandMapByID.put(island.getId(), island);
        }
    }

    @Override
    public void addEntry(Island island) {
        island.setChanged(true);
        islandMapByID.put(island.getId(), island);
    }

    public void deleteIsland(Island island) {
        islandMapByID.remove(island.getId());
    }

    @Override
    public void delete(Island island) {
        super.delete(island);
    }



    @Override
    public void clear() {
        islandMapByID.clear();
        IridiumSkyblock.getInstance().getLogger().info("Suppression de toute les données");
        super.clear();
    }

    public Optional<Island> getIsland(int id) {
        Island island = islandMapByID.get(id);
        if (island == null) return Optional.empty();
        return Optional.of(island);
    }


    public List<Island> getAllIslands() {
        return islandMapByID.values().stream().filter(Objects::nonNull).toList();
    }

    public List<Integer> getAllIslandID() {
        return islandMapByID.values().stream().filter(Objects::nonNull).map(Island::getId).toList();
    }

    public Optional<Island> getIslandByName(String name) {
        return islandMapByID.values().stream().filter(Objects::nonNull).filter(island -> island.getName().equalsIgnoreCase(name)).findFirst();
    }

    public LinkedHashMap<Integer, Island> getIslandMapByID() {
        return islandMapByID;
    }
}
