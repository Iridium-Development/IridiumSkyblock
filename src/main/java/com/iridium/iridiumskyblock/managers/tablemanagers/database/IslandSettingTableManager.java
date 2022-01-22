package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandSettingTableManager extends TableManager<IslandSetting, Integer> {

    LinkedHashMap<Integer, List<IslandSetting>> islandSettingById = new LinkedHashMap<>();

    public IslandSettingTableManager(ConnectionSource connectionSource, Class<IslandSetting> clazz, Comparator<IslandSetting> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandSetting> settingList = getEntries();
        for (int i = 0, settingSize = settingList.size(); i < settingSize; i++) {
            IslandSetting setting = settingList.get(i);
            List<IslandSetting> settings = islandSettingById.getOrDefault(setting.getIslandId(), new ArrayList<>());
            settings.add(setting);
            islandSettingById.put(setting.getIslandId(), settings);
        }
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
        List<IslandSetting> islandSettings = islandSettingById.getOrDefault(setting.getIslandId(), new ArrayList<>());
        islandSettings.remove(setting);
        islandSettingById.put(setting.getIslandId(), islandSettings);
        super.delete(setting);
    }

    @Override
    public void clear() {
        islandSettingById.clear();
        super.clear();
    }

    public Optional<IslandSetting> getEntry(IslandSetting islandSetting) {
        List<IslandSetting> islandSettings = islandSettingById.getOrDefault(islandSetting.getIslandId(), new ArrayList<>());
        if (islandSettings == null) return Optional.empty();
        for (IslandSetting setting : islandSettings) {
            if (setting.getSetting().equalsIgnoreCase(islandSetting.getSetting())) return Optional.of(setting);
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandSetting>> getIslandSettingById() {
        return islandSettingById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandSetting> getEntries(@NotNull Island island) {
        return islandSettingById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public void deleteDataByIsland(Island island) {
        List<IslandSetting> islandSettings = islandSettingById.getOrDefault(island.getId(), new ArrayList<>());
        islandSettingById.remove(island.getId());
        super.delete(islandSettings);
    }
}
