package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Setting;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.apache.commons.lang.WordUtils;
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
    }

}
