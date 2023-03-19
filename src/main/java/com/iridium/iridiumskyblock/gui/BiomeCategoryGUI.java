package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.biomes.BiomeCategory;
import com.iridium.iridiumskyblock.biomes.BiomeItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * GUI which shows all items in a {@link BiomeCategory} and allows players to purchase them.
 */
public class BiomeCategoryGUI extends GUI {

    private final BiomeCategory category;

    /**
     * The default constructor.
     *
     * @param category The category whose items should be displayed in this GUI
     */
    public BiomeCategoryGUI(BiomeCategory category, Inventory previousInventory) {
        super(previousInventory);
        this.category = category;
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, category.size, StringUtils.color(IridiumSkyblock.getInstance().getBiomes().categoryTitle
                .replace("%category_name%", category.name)
        ));

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> addContent(inventory));

        return inventory;
    }

    /**
     * Called when updating the Inventories contents
     */
    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getBiomes().categoryBackground);

        for (BiomeItem item : category.items) {
            ItemStack itemStack = item.item.parseItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemStack.setAmount(item.defaultAmount);
            itemMeta.setDisplayName(StringUtils.color(item.name));

            List<String> lore = item.lore == null ? new ArrayList<>() : new ArrayList<>(StringUtils.color(item.lore));
            addBiomeLore(lore, item);

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(item.slot, itemStack);
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
        }
    }

    /**
     * Called when there is a click in this GUI. Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<BiomeItem> clickedItem = category.items.stream()
                .filter(item -> item.slot == event.getSlot())
                .findAny();

        if (!clickedItem.isPresent()) {
            return;
        }

        // Perform the action corresponding to the click
        Player player = (Player) event.getWhoClicked();
        BiomeItem biomeItem = clickedItem.get();
        if (event.isLeftClick() && biomeItem.isPurchasable()) {
                IridiumSkyblock.getInstance().getBiomesManager().buy(player, biomeItem);
        } else {
            IridiumSkyblock.getInstance().getBiomes().failSound.play(player);
        }
    }

    private void addBiomeLore(List<String> lore, BiomeItem item) {
        if (item.isPurchasable()) {
            lore.add(
                    StringUtils.color(IridiumSkyblock.getInstance().getBiomes().buyPriceLore
                            .replace("%amount%", String.valueOf(item.defaultAmount))
                            .replace("%buy_price_vault%", formatPrice(item.buyCost.vault))
                            .replace("%buy_price_crystals%", formatPrice(item.buyCost.crystals))
                    )
            );
        } else {
            lore.add(StringUtils.color(IridiumSkyblock.getInstance().getBiomes().notPurchasableLore));
        }

        IridiumSkyblock.getInstance().getBiomes().biomeItemLore.stream()
                .map(StringUtils::color)
                .forEach(line -> lore.add(
                        line.replace("%amount%", String.valueOf(item.defaultAmount))
                ));
    }

    private String formatPrice(double value) {
        if (IridiumSkyblock.getInstance().getBiomes().abbreviatePrices) {
            return IridiumSkyblock.getInstance().getConfiguration().numberFormatter.format(value);
        } else {
            return String.valueOf(value);
        }
    }

}
