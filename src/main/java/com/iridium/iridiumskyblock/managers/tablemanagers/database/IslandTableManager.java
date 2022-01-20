package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class IslandTableManager extends TableManager<Island, Integer> {

    LinkedHashMap<Integer, Island> islandMapByID = new LinkedHashMap<>();

    public IslandTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Island.class, Comparator.comparing(Island::getId));
        System.out.println("Nouvelle Systeme des îles : ok");
        List<Island> islandsStatic = getEntries();
        for (int index = 0, sizeEntries = islandsStatic.size(); index < sizeEntries; index++) {
            Island island = islandsStatic.get(index);
            island.setChanged(false);
            islandMapByID.put(island.getId(), island);
        }
        System.out.println("Debug de Démarrage : " +
                "\nIle dans la base de donnée : " + islandsStatic +
                "\nIle dans la Map : " + islandMapByID.size());
    }

    @Override
    public void addEntry(Island island) {
        if (islandMapByID.containsKey(island.getId())) {
            island.setChanged(true);
            islandMapByID.replace(island.getId(), island);
            System.out.println("Remplacement de donnée");
        } else {
            island.setChanged(true);
            islandMapByID.put(island.getId(), island);
            System.out.println("Ajout de donnée dans la MAP");
        }
    }

    @Override
    public void delete(Island island) {
        islandMapByID.remove(island.getId()); // Todo ? A voir comment modifier ceci
        super.delete(island);
    }

    @Override
    public void clear() {
        islandMapByID.clear();
        System.out.println("Suppression de toute les données");
        super.clear();
    }

    public Optional<Island> getIsland(int id) {
        return Optional.ofNullable(islandMapByID.get(id));
    }

    public LinkedHashMap<Integer, Island> getIslandMapByID() {
        return islandMapByID;
    }
}
