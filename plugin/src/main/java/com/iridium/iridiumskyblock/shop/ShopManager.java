package com.iridium.iridiumskyblock.shop;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Shop.ShopCategoryConfig;
import com.iridium.iridiumskyblock.shop.ShopItem.BuyCost;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Handles the shop.
 */
public class ShopManager {

    private final List<ShopCategory> categories = new ArrayList<>();

    /**
     * The default constructor.
     *
     * Loads all categories and items.
     */
    public ShopManager() {
        for (String categoryName : IridiumSkyblock.getInstance().getShop().items.keySet()) {
            ShopCategoryConfig shopCategoryConfig = IridiumSkyblock.getInstance().getShop().categories.get(categoryName);
            if (shopCategoryConfig == null) {
                IridiumSkyblock.getInstance().getLogger().warning("Shop category " + categoryName + " is not configured, skipping...");
                continue;
            }

            List<String> lore = shopCategoryConfig.lore == null ? Collections.emptyList() : StringUtils.color(shopCategoryConfig.lore);

            categories.add(
                new ShopCategory(
                    categoryName,
                    StringUtils.color(shopCategoryConfig.formattedName),
                    shopCategoryConfig.representativeItem,
                    lore,
                    IridiumSkyblock.getInstance().getShop().items.get(categoryName),
                    shopCategoryConfig.slot
                )
            );
        }
    }

    /**
     * Returns a list of all loaded categories.
     *
     * @return All loaded categories of the shop
     */
    public List<ShopCategory> getCategories() {
        return categories;
    }

    /**
     * Returns the category with the provided name, null if there is none.
     *
     * @param name The name of the category
     * @return The category with the name
     */
    public Optional<ShopCategory> getCategoryByName(String name) {
        return categories.stream()
            .filter(category -> name.equals(category.name))
            .findAny();
    }

    /**
     * Returns the category with the provided name containing colors, null if there is none.
     *
     * @param formattedName The formatted name of the category
     * @return The category with the name
     */
    public Optional<ShopCategory> getCategoryByFormattedName(String formattedName) {
        return categories.stream()
            .filter(category -> StringUtils.color(formattedName).equals(category.formattedName))
            .findAny();
    }

    /**
     * Buys an item for the Player in the shop.
     * He might not have enough money to do so.
     *
     * @param player The player which wants to buy the item
     * @param shopItem The item which is requested
     * @param amount The amount of the item which is requested
     */
    public void buy(Player player, ShopItem shopItem, int amount) {
        BuyCost buyCost = shopItem.buyCost;
        double vaultCost = calculateCost(amount, shopItem.defaultAmount, buyCost.vault);
        int crystalCost = (int) calculateCost(amount, shopItem.defaultAmount, buyCost.crystals);

        // TODO: Check if player has money + crystals, Remove them

        if (shopItem.command == null) {
            // Add item to the player Inventory
            if (!InventoryUtils.hasEmptySlot(player.getInventory())) {
                player.sendMessage(
                    StringUtils.color(
                        IridiumSkyblock.getInstance().getMessages().cannotAfford
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    )
                );
                return;
            }

            ItemStack itemStack = shopItem.type.parseItem();
            itemStack.setAmount(amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(StringUtils.color(shopItem.name));
            itemStack.setItemMeta(itemMeta);

            player.getInventory().addItem(itemStack);
        } else {
            // Run the command
            String command = shopItem.command
                .replace("%player%", player.getName())
                .replace("%amount%", String.valueOf(amount));

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    /**
     * Sells an item for the Player in the shop.
     * He might not meet all requirements to do so.
     *
     * @param player The player which wants to sell the item
     * @param shopItem The item which is to be sold
     * @param amount The amount of the item which is to be sold
     */
    public void sell(Player player, ShopItem shopItem, int amount) {
        // TODO: Check if player has item in the Inventory, Remove it, Give Reward
    }

    /**
     * Calculates the cost of an item with the provided amount given the default price and amount.
     *
     * @param amount The amount which should be calculated
     * @param defaultAmount The default amount of the item
     * @param defaultPrice The default price of the item
     * @return The price of the item in the given quantity
     */
    public double calculateCost(int amount, int defaultAmount, double defaultPrice) {
        double costPerItem = defaultPrice / defaultAmount;
        return costPerItem * amount;
    }

}
