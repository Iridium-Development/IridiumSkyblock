package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBankTableManager extends TableManager<IslandBank, Integer> {

    // Comparator.comparing(IslandBank::getIslandId).thenComparing(IslandBank::getBankItem)

    LinkedHashMap<Integer, List<IslandBank>> islandBankById = new LinkedHashMap<>();

    public IslandBankTableManager(ConnectionSource connectionSource, Class<IslandBank> clazz, Comparator<IslandBank> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBank> bankList = getEntries();
        for (int i = 0, bankSize = bankList.size(); i < bankSize; i++) {
            IslandBank bank = bankList.get(i);
            List<IslandBank> banks = islandBankById.getOrDefault(bank.getIslandId(), new ArrayList<>());
            banks.add(bank);
            islandBankById.put(bank.getIslandId(), banks);
        }
    }

    @Override
    public void addEntry(IslandBank islandBank) {
        islandBank.setChanged(true);
        List<IslandBank> islandBanks = islandBankById.getOrDefault(islandBank.getIslandId(), new ArrayList<>());
        if (islandBanks == null) islandBanks = new ArrayList<>();
        islandBanks.add(islandBank);
        islandBankById.put(islandBank.getIslandId(), islandBanks);
    }

    @Override
    public void delete(IslandBank islandBank) {
        islandBankById.put(islandBank.getIslandId(), null);
        super.delete(islandBank);
    }

    @Override
    public void clear() {
        islandBankById.clear();
        super.clear();
    }

    public Optional<IslandBank> getEntry(IslandBank islandBank) {
        List<IslandBank> banks = islandBankById.get(islandBank.getIslandId());
        if (banks == null || banks.isEmpty()) return Optional.empty();
        for (IslandBank bank : banks) {
            if (bank.getBankItem().equalsIgnoreCase(islandBank.getBankItem())) return Optional.of(bank);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandBank>> getIslandBankById() {
        return islandBankById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandBank> getEntries(@NotNull Island island) {
        List<IslandBank> bankList = islandBankById.getOrDefault(island.getId(), new ArrayList<>());
        return bankList == null ? new ArrayList<>() : bankList;
    }
}
