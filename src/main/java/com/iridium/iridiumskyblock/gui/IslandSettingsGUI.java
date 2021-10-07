package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.settings.IslandSettingImpl;
import com.iridium.iridiumskyblock.settings.IslandTime;
import com.iridium.iridiumskyblock.settings.IslandWeather;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * GUI which allows users to alter the Island's permissions.
 */
public class IslandSettingsGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandSettingsGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandSettingsGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSettingsGUI.background);

        for (Map.Entry<String, IslandSettingImpl> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (!setting.getValue().isEnabled()) return;
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
        User user = IridiumSkyblock.getInstance().getUserManager().getUser((OfflinePlayer) event.getWhoClicked());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(getIsland(), user, PermissionType.ISLAND_SETTINGS)) {
            event.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotChangeSettings.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        for (Map.Entry<String, IslandSettingImpl> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (!setting.getValue().isEnabled() || !setting.getValue().isChangeable() || event.getSlot() != setting.getValue().getItem().slot) continue;
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), setting.getKey(), setting.getValue().getDefaultValue());
            Enum<?> newValue = event.isRightClick() ? setting.getValue().getNext(islandSetting.getValue()) : setting.getValue().getPrevious(islandSetting.getValue());
            islandSetting.setValue(newValue.name());
            if (setting.getValue() instanceof IslandWeather) {
                IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                    if (newValue.equals(IslandWeather.IslandWeatherTypes.DEFAULT)) {
                        player.resetPlayerWeather();
                    } else {
                        player.setPlayerWeather(newValue.equals(IslandWeather.IslandWeatherTypes.CLEAR) ? WeatherType.CLEAR : WeatherType.DOWNFALL);
                    }
                });
            } else if (setting.getValue() instanceof IslandTime) {
                IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                    player.setPlayerTime(((IslandTime.IslandTimeTypes) newValue).getTime(), ((IslandTime.IslandTimeTypes) newValue).isRelative());
                });
            }
        }
    }

}
