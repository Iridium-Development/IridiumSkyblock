package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.database.Island;

import java.util.concurrent.atomic.AtomicInteger;

public class IslandBuilder {
    private final static AtomicInteger ISLAND_ID = new AtomicInteger(1);
    private final Island island;

    public IslandBuilder() {
        int id = ISLAND_ID.getAndIncrement();
        this.island = new Island("Island_" + id, id);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().addEntry(island);
    }

    public IslandBuilder(int id) {
        this.island = new Island("Island_" + id, id);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().addEntry(island);
    }

    public IslandBuilder(String name) {
        this.island = new Island(name, ISLAND_ID.getAndIncrement());
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().addEntry(island);
    }

    public Island build() {
        return island;
    }
}
