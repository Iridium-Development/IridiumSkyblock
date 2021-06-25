package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BiomeGUI extends GUI {

    private final List<XBiome> biomes;
    private final int page;
    private final Island island;

    public BiomeGUI(int page, Island island) {
        this.biomes = XBiome.VALUES;
        this.page = page;
        this.island = island;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        final int size = this.getInventory().getSize();

        if (event.getSlot() == size - 7 && page > 1) {
            event.getWhoClicked().openInventory(new BiomeGUI(page - 1, this.island).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < biomes.size()) {
            event.getWhoClicked().openInventory(new BiomeGUI(page + 1, this.island).getInventory());
            return;
        }

        if (event.getSlot() + 1 <= biomes.size()) {
            int index = ((size - 9) * (page - 1)) + event.getSlot();

            if (biomes.size() > index) {
                IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();

                XBiome biome = biomes.get(index);
                islandManager.getIslandChunks(island, islandManager.getWorld())
                        .thenAccept(chunks -> chunks.forEach(biome::setBiome));

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%biome%", WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " ")))));

            }

        }

    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().biomeGUI.background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        final long elementsPerPage = inventory.getSize() - 9;
        AtomicInteger index = new AtomicInteger(0);

        biomes.stream()
                .skip((page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(biome -> {
                    final ItemStack item = ItemStackUtils.makeItem(XMaterial.GRASS_BLOCK, 1, "&b&l" + WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " ")), Arrays.asList("&7Click to change", "&7your island biome!"));
                    inventory.setItem(index.getAndIncrement(), item);
                });

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        final NoItemGUI gui = IridiumSkyblock.getInstance().getInventories().biomeGUI;
        final Inventory inventory = Bukkit.createInventory(this, gui.size, StringUtils.color(gui.title));

        this.addContent(inventory);
        return inventory;
    }

}
