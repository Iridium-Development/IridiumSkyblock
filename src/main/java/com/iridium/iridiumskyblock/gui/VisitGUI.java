package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.SingleItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.SkinUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GUI which shows a list of all Islands a user can visit.
 */
public class VisitGUI implements GUI {

    private static SortedMap<Integer, VisitGUI> cache;

    public static void invalidateCache() {
        cache = new TreeMap<>() ;
    }

    public static VisitGUI getVisitGui(int page) {
        return cache.containsKey(page) ? cache.get(page) : new VisitGUI(page);
    }

    private List<Island> islands;
    private final int page;
    private final Inventory inventory;

    /**
     * The default constructor.
     *
     * @param page The current page of this GUI
     */
    private VisitGUI(int page) {
        this.page = page;
        cache.put(page, this);
        SingleItemGUI singleItemGUI = IridiumSkyblock.getInstance().getInventories().visitGUI;
        inventory = Bukkit.createInventory(this, singleItemGUI.size, StringUtils.color(singleItemGUI.title));
        addContent(inventory);
    }

    /**
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().visitGUI.background);
        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        int elementsPerPage = inventory.getSize() - 9;
        AtomicInteger slot = new AtomicInteger(0);
        if (IridiumSkyblock.getInstance().getInventories().visitGUI.item.material == XMaterial.PLAYER_HEAD
                && IridiumSkyblock.getInstance().getInventories().visitGUI.item.headData == null) {
            Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                this.islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream()
                        .filter(Island::isVisitable).collect(Collectors.toList());
                LinkedHashMap<UUID, ItemStack> initialItems = new LinkedHashMap<>();
                LinkedHashMap<UUID, Item> querySkins = new LinkedHashMap<>();
                LinkedHashMap<UUID, List<Placeholder>> querySkinsPlaceHolders = new LinkedHashMap<>();

                islands.stream()
                        .skip((long) (page - 1) * elementsPerPage)
                        .limit(elementsPerPage)
                        .forEachOrdered(island -> {
                            Item initialItem = new Item(
                                    XMaterial.PLAYER_HEAD,
                                    slot.getAndIncrement(),
                                    SkinUtils.getHeadData(island.getOwner().getUuid()),
                                    IridiumSkyblock.getInstance().getInventories().visitGUI.item.amount,
                                    IridiumSkyblock.getInstance().getInventories().visitGUI.item.displayName,
                                    IridiumSkyblock.getInstance().getInventories().visitGUI.item.lore
                            );
                            List<Placeholder> placeholderList = new PlaceholderBuilder().applyIslandPlaceholders(island).build();
                            if (initialItem.headData == null) {
                                initialItem.headData = IridiumSkyblock.getInstance().getConfiguration().headDataLoadingSkin;
                                querySkins.put(island.getOwner().getUuid(), initialItem);
                                querySkinsPlaceHolders.put(island.getOwner().getUuid(), placeholderList);
                            }
                            initialItems.put(island.getOwner().getUuid(), ItemStackUtils.makeItem(initialItem, placeholderList));
                        });

                slot.set(0);
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                        initialItems.values().forEach(item ->
                                inventory.setItem(slot.getAndIncrement(), item)));

                querySkins.keySet().forEach(uuid -> Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                    String headData = SkinUtils.queryHeadData(uuid);
                    if (headData == null) return;
                    Item itemWithSkin = querySkins.get(uuid);
                    itemWithSkin.headData = headData;
                    ItemStack itemStackWithSkin = ItemStackUtils.makeItem(itemWithSkin, querySkinsPlaceHolders.get(uuid));
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> inventory.setItem(itemWithSkin.slot, itemStackWithSkin));
                }));
            });
        } else {
            islands.stream()
                    .skip((long) (page - 1) * elementsPerPage)
                    .limit(elementsPerPage)
                    .forEachOrdered(island -> inventory.setItem(slot.getAndIncrement(),
                            ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitGUI.item,
                                    new PlaceholderBuilder().applyIslandPlaceholders(island).build())));
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
        if (event.getSlot() == getInventory().getSize() - 7) {
            if (page > 1) {
                event.getWhoClicked().openInventory(new VisitGUI(page - 1).getInventory());
            }
        } else if (event.getSlot() == getInventory().getSize() - 3) {
            if ((event.getInventory().getSize() - 9) * page < islands.size()) {
                event.getWhoClicked().openInventory(new VisitGUI(page + 1).getInventory());
            }
        } else if (islands != null && event.getSlot() + 1 <= islands.size()) {
            int index = ((event.getInventory().getSize() - 9) * (page - 1)) + event.getSlot();
            if (islands.size() > index) {
                Island island = islands.get(index);
                String command = IridiumSkyblock.getInstance().getCommands().visitCommand.aliases.get(0);
                Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + island.getOwner().getName());
                event.getWhoClicked().closeInventory();
            }
        }
    }

}