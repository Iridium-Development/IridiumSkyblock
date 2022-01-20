package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBanTableManager extends TableManager<IslandBan, Integer> {

    // Comparator.comparing(IslandBan::getIslandId).thenComparing(islandBan -> islandBan.getBannedUser() != null ? islandBan.getBannedUser().getUuid() : null)

    LinkedHashMap<Integer, List<IslandBan>> islandBanById = new LinkedHashMap<>();

    public IslandBanTableManager(ConnectionSource connectionSource, Class<IslandBan> clazz, Comparator<IslandBan> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBan> bankList = getEntries();
        for (int i = 0, bankSize = bankList.size(); i < bankSize; i++) {
            IslandBan bank = bankList.get(i);
            List<IslandBan> banks = islandBanById.getOrDefault(bank.getIslandId(), new ArrayList<>());
            banks.add(bank);
            islandBanById.put(bank.getIslandId(), banks);
        }

        int valueReward = 0;
        for (List<IslandBan> islandBanks : islandBanById.values()) {
            valueReward = islandBanks.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandBan islandBan) {
        islandBan.setChanged(true);
        List<IslandBan> islandBanks = islandBanById.getOrDefault(islandBan.getIslandId(), new ArrayList<>());
        islandBanks.add(islandBan);
        islandBanById.put(islandBan.getIslandId(), islandBanks);
    }

    @Override
    public void delete(IslandBan islandBan) {
        islandBanById.put(islandBan.getIslandId(), null);
        super.delete(islandBan);
    }

    @Override
    public void clear() {
        islandBanById.clear();
        super.clear();
    }

    public Optional<IslandBan> getEntry(IslandBan islandBan) {
        List<IslandBan> islandBans = islandBanById.get(islandBan.getIslandId());
        if (islandBans == null) return Optional.empty();
        for (IslandBan ban : islandBans) {
            if (ban.getBannedUser() != null) {
                if (islandBan.getBannedUser() != null) {
                    if (ban.getBannedUser().getUuid().equals(islandBan.getBannedUser().getUuid())) return Optional.of(ban);
                }
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandBan>> getIslandBanById() {
        return islandBanById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandBan> getEntries(@NotNull Island island) {
        List<IslandBan> banList = islandBanById.getOrDefault(island.getId(), new ArrayList<>());
        return banList == null ? new ArrayList<>() : banList;
    }
}
