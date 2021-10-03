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
import com.iridium.iridiumskyblock.settings.*;
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

        for (Map.Entry<String, IslandSettingConfig> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
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

        for (Map.Entry<String, IslandSettingConfig> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (event.getSlot() != setting.getValue().getItem().slot) continue;

            IslandSettingType islandSettingType = IslandSettingType.getByName(setting.getKey());
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), islandSettingType);
            switch (islandSettingType) {
                case TIME:
                    IslandTime currentTime = IslandTime.valueOf(islandSetting.getValue());
                    IslandTime nextTime = event.isRightClick() ? currentTime.getNext() : currentTime.getPrevious();
                    IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                        player.setPlayerTime(nextTime.getTime(), nextTime.isRelative());
                    });
                    islandSetting.setValue(nextTime.name());
                    break;
                case WEATHER:
                    IslandWeather currentWeather = IslandWeather.valueOf(islandSetting.getValue());
                    IslandWeather nextWeather = event.isRightClick() ? currentWeather.getNext() : currentWeather.getPrevious();
                    IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream().map(User::getPlayer).forEach(player -> {
                        if (nextWeather.equals(IslandWeather.DEFAULT)) {
                            player.resetPlayerWeather();
                        } else {
                            player.setPlayerWeather(nextWeather == IslandWeather.CLEAR ? WeatherType.CLEAR : WeatherType.DOWNFALL);
                        }
                    });
                    islandSetting.setValue(nextWeather.name());
                    break;
                case MOB_SPAWN:
                    IslandMobSpawn currentMobSpawn = IslandMobSpawn.valueOf(islandSetting.getValue());
                    IslandMobSpawn nextMobSpawn = event.isRightClick() ? currentMobSpawn.getNext() : currentMobSpawn.getPrevious();
                    islandSetting.setValue(nextMobSpawn.name());
                    break;
                default:
                    islandSetting.setValue((islandSetting.getValue().equalsIgnoreCase("ALLOWED") ? IslandSwitchSetting.DISALLOWED : IslandSwitchSetting.ALLOWED).name());
                    break;
            }
            addContent(event.getInventory());
            break;
        }
    }

}
