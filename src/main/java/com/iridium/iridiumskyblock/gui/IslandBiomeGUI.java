package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IslandBiomeGUI extends IslandGUI {

    private final List<XBiome> biomes;
    private final int page;
    private final World.Environment environment;
    private final CooldownProvider<CommandSender> cooldownProvider;

    public IslandBiomeGUI(int page, Island island, World.Environment environment, CooldownProvider<CommandSender> cooldownProvider) {
        super(IridiumSkyblock.getInstance().getInventories().biomeGUI, island);
        this.biomes = Arrays.stream(XBiome.VALUES).filter(biome -> biome.getEnvironment() == environment).collect(Collectors.toList());
        this.environment = environment;
        this.page = page;
        this.cooldownProvider = cooldownProvider;
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        final long elementsPerPage = inventory.getSize() - 9;
        AtomicInteger index = new AtomicInteger(0);

        biomes.stream()
                .filter(xBiome -> xBiome.getBiome() != null)
                .skip((page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(biome ->
                        inventory.setItem(index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().biomeGUI.item, Collections.singletonList(new Placeholder("biome", WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " "))))))
                );
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        final int size = IridiumSkyblock.getInstance().getInventories().biomeGUI.size;

        if (event.getSlot() == size - 7 && page > 1) {
            player.openInventory(new IslandBiomeGUI(page - 1, getIsland(), environment, cooldownProvider).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < biomes.size()) {
            player.openInventory(new IslandBiomeGUI(page + 1, getIsland(), environment, cooldownProvider).getInventory());
            return;
        }

        if (event.getSlot() + 1 <= biomes.size()) {
            int index = ((size - 9) * (page - 1)) + event.getSlot();
            if (biomes.size() > index) {
                XBiome biome = biomes.get(index);
                String command = IridiumSkyblock.getInstance().getCommands().biomeCommand.aliases.get(0);
                Bukkit.dispatchCommand(player, "is " + command + " " + biome.toString());
                player.closeInventory();
                cooldownProvider.applyCooldown(player);
            }
        }
    }

}
