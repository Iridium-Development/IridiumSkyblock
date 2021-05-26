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

public class ShopManager {

    private final List<ShopCategory> categories = new ArrayList<>();

    public ShopManager() {
        for (String categoryName : IridiumSkyblock.getInstance().getShop().items.keySet()) {
            ShopCategoryConfig shopCategoryConfig = IridiumSkyblock.getInstance().getShop().categories.get(categoryName);
            if (shopCategoryConfig == null) {
                System.out.println("ERROR: Shop category " + categoryName + " is not configured");
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

    public List<ShopCategory> getCategories() {
        return categories;
    }

    public Optional<ShopCategory> getCategoryByName(String name) {
        return categories.stream()
            .filter(category -> name.equals(category.name))
            .findAny();
    }

    public Optional<ShopCategory> getCategoryByFormattedName(String formattedName) {
        return categories.stream()
            .filter(category -> StringUtils.color(formattedName).equals(category.formattedName))
            .findAny();
    }

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

    public void sell(Player player, ShopItem shopItem, int amount) {
        // TODO: Check if player has item in the Inventory, Remove it, Give Reward
    }

    public double calculateCost(int amount, int defaultAmount, double defaultPrice) {
        double costPerItem = defaultPrice / defaultAmount;
        return costPerItem * amount;
    }

}
