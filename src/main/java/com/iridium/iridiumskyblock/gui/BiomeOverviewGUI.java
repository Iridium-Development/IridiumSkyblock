package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BiomeOverviewGUI extends BackGUI {

    public BiomeOverviewGUI(Player player) {
        super(IridiumSkyblock.getInstance().getInventories().biomeOverviewGUI.background, player, IridiumSkyblock.getInstance().getInventories().backButton);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI biomeOverviewGUI = IridiumSkyblock.getInstance().getInventories().biomeOverviewGUI;
        Inventory inventory = Bukkit.createInventory(this, biomeOverviewGUI.size, StringUtils.color(biomeOverviewGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        super.addContent(inventory);

        for (Biomes.BiomeCategory category : IridiumSkyblock.getInstance().getBiomes().categories.values()) {
            inventory.setItem(category.item.slot, ItemStackUtils.makeItem(category.item));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (Map.Entry<String, Biomes.BiomeCategory> category : IridiumSkyblock.getInstance().getBiomes().categories.entrySet()) {
            if (event.getSlot() != category.getValue().item.slot) continue;
            event.getWhoClicked().openInventory(new BiomeCategoryGUI(category.getKey(), (Player) event.getWhoClicked()).getInventory());
            return;
        }
        super.onInventoryClick(event);
    }
}


