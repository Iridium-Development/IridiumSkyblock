package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.IslandTopInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.SkinUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * GUI which shows the Islands with the highest Island value.
 *
 * @see Island#getValue()
 */
public class IslandTopGUI implements GUI {

    private final HashMap<Integer, Island> islandSlots = new HashMap<>();
    private final Inventory inventory;

    /**
     * The default constructor.
     */
    public IslandTopGUI() {
        List<Island> islands = IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE);

        for (int rank : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.keySet()) {
            if (islands.size() < rank) continue;
            islandSlots.put(IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.get(rank), islands.get(rank - 1));
        }
        IslandTopInventoryConfig topInventoryConfig = IridiumSkyblock.getInstance().getInventories().islandTopGUI;
        inventory = Bukkit.createInventory(this, topInventoryConfig.size, StringUtils.color(topInventoryConfig.title));

        addContent(inventory);
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandTopGUI.background);

        for (int slot : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.values()) {
            if (islandSlots.containsKey(slot)) {
                Island island = islandSlots.get(slot);
                List<Placeholder> placeholderList = new PlaceholderBuilder().applyIslandPlaceholders(island).build();
                if (IridiumSkyblock.getInstance().getInventories().islandTopGUI.item.material == XMaterial.PLAYER_HEAD
                        && IridiumSkyblock.getInstance().getInventories().islandTopGUI.item.headData == null) {
                    Item initialItem = new Item(
                            XMaterial.PLAYER_HEAD,
                            slot,
                            SkinUtils.getHeadData(island.getOwner().getUuid()),
                            IridiumSkyblock.getInstance().getInventories().islandTopGUI.item.amount,
                            IridiumSkyblock.getInstance().getInventories().islandTopGUI.item.displayName,
                            IridiumSkyblock.getInstance().getInventories().islandTopGUI.item.lore
                    );
                    boolean querySkin = false;
                    if (initialItem.headData == null) {
                        initialItem.headData = IridiumSkyblock.getInstance().getConfiguration().headDataLoadingSkin;
                        querySkin = true;
                    }
                    inventory.setItem(slot, ItemStackUtils.makeItem(initialItem, placeholderList));
                    if (querySkin) Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                        String headData = SkinUtils.queryHeadData(island.getOwner().getUuid());
                        if (headData == null) return;
                        initialItem.headData = headData;
                        ItemStack itemStackWithSkin = ItemStackUtils.makeItem(initialItem, placeholderList);
                        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> inventory.setItem(slot, itemStackWithSkin));
                    });
                } else {
                    inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandTopGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build()));
                }
            } else {
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandTopGUI.filler));
            }
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
        if (!islandSlots.containsKey(event.getSlot())) return;

        Island island = islandSlots.get(event.getSlot());
        String command = IridiumSkyblock.getInstance().getCommands().visitCommand.aliases.get(0);
        Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + island.getOwner().getName());
    }

}
