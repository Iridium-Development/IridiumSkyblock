package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandSettingTableManager extends TableManager<IslandSetting, Integer> {

    // Comparator.comparing(IslandSetting::getIslandId).thenComparing(IslandSetting::getSetting)

    LinkedHashMap<Integer, List<IslandSetting>> islandSettingById = new LinkedHashMap<>();

    public IslandSettingTableManager(ConnectionSource connectionSource, Class<IslandSetting> clazz, Comparator<IslandSetting> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandSetting> bankList = getEntries();
        for (int i = 0, bankSize = bankList.size(); i < bankSize; i++) {
            IslandSetting setting = bankList.get(i);
            List<IslandSetting> settings = islandSettingById.getOrDefault(setting.getIslandId(), new ArrayList<>());
            settings.add(setting);
            islandSettingById.put(setting.getIslandId(), settings);
        }

        int valueReward = 0;
        for (List<IslandSetting> islandBanks : islandSettingById.values()) {
            valueReward = islandBanks.size();
        }
        System.out.println("Nombre de Warps en attente dans la base de donnée: " + getEntries().size() + "\n" +
                "Nombre de reward en attente final " + valueReward);
    }

    @Override
    public void addEntry(IslandSetting setting) {
        setting.setChanged(true);
        List<IslandSetting> islandSettings = islandSettingById.getOrDefault(setting.getIslandId(), new ArrayList<>());
        islandSettings.add(setting);
        islandSettingById.put(setting.getIslandId(), islandSettings);
    }

    @Override
    public void delete(IslandSetting setting) {
        islandSettingById.put(setting.getIslandId(), null);
        super.delete(setting);
    }

    @Override
    public void clear() {
        islandSettingById.clear();
        super.clear();
    }

    public Optional<IslandSetting> getEntry(IslandSetting islandSetting) {
        List<IslandSetting> islandSettings = islandSettingById.get(islandSetting.getIslandId());
        if (islandSettings == null) return Optional.empty();
        for (IslandSetting setting : islandSettings) {
            if (setting.getSetting().equalsIgnoreCase(islandSetting.getSetting())) return Optional.of(setting);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandSetting>> get() {
        return islandSettingById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandSetting> getEntries(@NotNull Island island) {
        List<IslandSetting> islandSettings = islandSettingById.getOrDefault(island.getId(), new ArrayList<>());
        return islandSettings == null ? new ArrayList<>() : islandSettings;
    }
}
