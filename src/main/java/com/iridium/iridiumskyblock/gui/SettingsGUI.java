package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import org.apache.commons.lang.WordUtils;
import org.bukkit.WeatherType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * GUI which allows users to alter the Island's permissions.
 */
public class SettingsGUI extends GUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public SettingsGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandSettingsGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSettingsGUI.background);

        for (Map.Entry<String, Setting> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), setting.getKey(), setting.getValue().getDefaultValue());
            inventory.setItem(setting.getValue().getItem().slot, ItemStackUtils.makeItem(setting.getValue().getItem(), Collections.singletonList(new Placeholder("value", WordUtils.capitalize(islandSetting.getValue().toLowerCase().replace("_", " "))))));
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (Map.Entry<String, Setting> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (event.getSlot() != setting.getValue().getItem().slot) continue;
            SettingType settingType = SettingType.getByName(setting.getKey());
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), settingType);
            String newValue = (event.getClick() == ClickType.RIGHT ? settingType.getNext() : settingType.getPrevious()).getNew(islandSetting.getValue());
            islandSetting.setValue(newValue);
            if (setting.getKey().equalsIgnoreCase(SettingType.TIME.getSettingName())) {
                IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                    IslandTime islandTime = IslandTime.valueOf(newValue);
                    player.setPlayerTime(islandTime.getTime(), islandTime.isRelative());
                });
            }
            if (setting.getKey().equalsIgnoreCase(SettingType.WEATHER.getSettingName())) {
                IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                    IslandWeatherType islandWeatherType = IslandWeatherType.valueOf(newValue);
                    if (islandWeatherType == IslandWeatherType.DEFAULT) {
                        player.resetPlayerWeather();
                    } else {
                        player.setPlayerWeather(islandWeatherType == IslandWeatherType.CLEAR ? WeatherType.CLEAR : WeatherType.DOWNFALL);
                    }
                });
            }
            addContent(event.getInventory());
        }
    }

}
