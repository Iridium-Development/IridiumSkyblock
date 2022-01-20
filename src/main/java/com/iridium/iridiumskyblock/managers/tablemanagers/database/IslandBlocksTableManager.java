package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBlocksTableManager extends TableManager<IslandBlocks, Integer> {

    LinkedHashMap<Integer, List<IslandBlocks>> islandBlockById = new LinkedHashMap<>();

    public IslandBlocksTableManager(ConnectionSource connectionSource, Class<IslandBlocks> clazz, Comparator<IslandBlocks> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBlocks> blocksList = getEntries();
        for (int i = 0, BlockSize = blocksList.size(); i < BlockSize; i++) {
            IslandBlocks block = blocksList.get(i);
            List<IslandBlocks> blocks = islandBlockById.getOrDefault(block.getIslandId(), new ArrayList<>());
            blocks.add(block);
            islandBlockById.put(block.getIslandId(), blocks);
        }
    }

    @Override
    public void addEntry(IslandBlocks islandBlocks) {
        islandBlocks.setChanged(true);
        List<IslandBlocks> blocksList = islandBlockById.getOrDefault(islandBlocks.getIslandId(), new ArrayList<>());
        if (blocksList == null) blocksList = new ArrayList<>();
        blocksList.add(islandBlocks);
        islandBlockById.put(islandBlocks.getIslandId(), blocksList);
    }

    @Override
    public void delete(IslandBlocks islandBlocks) {
        islandBlockById.put(islandBlocks.getIslandId(), null);
        super.delete(islandBlocks);
    }

    @Override
    public void clear() {
        islandBlockById.clear();
        super.clear();
    }

    public Optional<IslandBlocks> getEntry(IslandBlocks islandBlocks) {
        List<IslandBlocks> blocks = islandBlockById.get(islandBlocks.getIslandId());
        if (blocks == null) return Optional.empty();
        for (IslandBlocks block : blocks) {
            if (islandBlocks.getMaterial().equals(block.getMaterial())) return Optional.of(block);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandBlocks>> getIslandBlockById() {
        return islandBlockById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandBlocks> getEntries(@NotNull Island island) {
        List<IslandBlocks> blocksList = islandBlockById.getOrDefault(island.getId(), new ArrayList<>());
        return blocksList == null ? new ArrayList<>() : blocksList;
    }
}
