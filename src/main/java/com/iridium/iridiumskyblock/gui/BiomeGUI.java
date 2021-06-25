package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BiomeGUI extends GUI {

    private final List<XBiome> biomes;
    private final int page;
    private final Island island;
    private final World.Environment environment;

    public BiomeGUI(int page, Island island, World.Environment environment) {
        super(IridiumSkyblock.getInstance().getInventories().biomeGUI);
        this.biomes = XBiome.VALUES.stream().filter(biome -> biome.getEnvironment() == environment).collect(Collectors.toList());
        this.environment = environment;
        this.page = page;
        this.island = island;
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
                .forEachOrdered(biome ->
                        inventory.setItem(index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().biomeGUI.item, Collections.singletonList(new Placeholder("biome", WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " "))))))
                );

    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

        final int size = IridiumSkyblock.getInstance().getInventories().biomeGUI.size;

        if (event.getSlot() == size - 7 && page > 1) {
            event.getWhoClicked().openInventory(new BiomeGUI(page - 1, this.island, environment).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < biomes.size()) {
            event.getWhoClicked().openInventory(new BiomeGUI(page + 1, this.island, environment).getInventory());
            return;
        }

        if (event.getSlot() + 1 <= biomes.size()) {
            int index = ((size - 9) * (page - 1)) + event.getSlot();
            if (biomes.size() > index) {
                XBiome biome = biomes.get(index);
                String command = IridiumSkyblock.getInstance().getCommands().biomeCommand.aliases.get(0);
                Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + biome.toString());
                event.getWhoClicked().closeInventory();

            }
        }
    }

}
