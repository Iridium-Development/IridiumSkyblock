package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.Setting;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IslandSettingChangeEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
    public IslandSettingsGUI(@NotNull Island island, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().islandSettingsGUI, previousInventory, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSettingsGUI.background);

        for (Map.Entry<String, Setting> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (!setting.getValue().isModifiable()) continue;

            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), setting.getKey(), setting.getValue().getDefaultValue());
            inventory.setItem(setting.getValue().getItem().slot, ItemStackUtils.makeItem(setting.getValue().getItem(), Collections.singletonList(new Placeholder("value", WordUtils.capitalize(islandSetting.getValue().toLowerCase().replace("_", " "))))));
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
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
        Player player = (Player) event.getWhoClicked();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(getIsland(), user, PermissionType.ISLAND_SETTINGS)) {
            event.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotChangeSettings.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        for (Map.Entry<String, Setting> setting : IridiumSkyblock.getInstance().getSettingsList().entrySet()) {
            if (event.getSlot() != setting.getValue().getItem().slot) continue;
            if (!setting.getValue().isModifiable()) continue;

            SettingType settingType = SettingType.getByName(setting.getKey());
            IslandSetting islandSetting = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(getIsland(), settingType);
            String newValue = (event.getClick() == ClickType.RIGHT ? settingType.getNext() : settingType.getPrevious()).getNew(islandSetting.getValue());

            IslandSettingChangeEvent islandSettingChangeEvent = new IslandSettingChangeEvent(player, getIsland(), settingType, newValue);
            Bukkit.getPluginManager().callEvent(islandSettingChangeEvent);
            if (islandSettingChangeEvent.isCancelled()) return;

            newValue = islandSettingChangeEvent.getNewValue();
            islandSetting.setValue(newValue);
            settingType.getOnChange().run(getIsland(), newValue);
            addContent(event.getInventory());
        }
    }

}
