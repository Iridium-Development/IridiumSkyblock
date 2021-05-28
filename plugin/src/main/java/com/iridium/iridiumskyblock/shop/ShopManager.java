package com.iridium.iridiumskyblock.shop;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Shop.ShopCategoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.shop.ShopItem.BuyCost;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the shop.
 */
public class ShopManager {

    private final List<ShopCategory> categories = new ArrayList<>();

    /**
     * The default constructor.
     * <p>
     * Loads all categories and items.
     */
    public ShopManager() {
        for (String categoryName : IridiumSkyblock.getInstance().getShop().items.keySet()) {
            ShopCategoryConfig shopCategoryConfig = IridiumSkyblock.getInstance().getShop().categories.get(categoryName);
            if (shopCategoryConfig == null) {
                IridiumSkyblock.getInstance().getLogger().warning("Shop category " + categoryName + " is not configured, skipping...");
                continue;
            }

            categories.add(
                    new ShopCategory(
                            categoryName,
                            shopCategoryConfig.item,
                            IridiumSkyblock.getInstance().getShop().items.get(categoryName),
                            shopCategoryConfig.inventoryRows * 9
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
     * @param slot The slot of the category
     * @return The category with the name
     */
    public Optional<ShopCategory> getCategoryBySlot(int slot) {
        return categories.stream()
                .filter(category -> category.item.slot == slot)
                .findAny();
    }

    /**
     * Buys an item for the Player in the shop.
     * He might not have enough money to do so.
     *
     * @param player   The player which wants to buy the item
     * @param shopItem The item which is requested
     * @param amount   The amount of the item which is requested
     */
    public void buy(Player player, ShopItem shopItem, int amount) {
        BuyCost buyCost = shopItem.buyCost;
        double vaultCost = calculateCost(amount, shopItem.defaultAmount, buyCost.vault);
        int crystalCost = (int) calculateCost(amount, shopItem.defaultAmount, buyCost.crystals);

        boolean canPurchase = PlayerUtils.pay(
                player,
                IridiumSkyblockAPI.getInstance().getUser(player).getIsland().get(),
                crystalCost,
                vaultCost
        );

        if (!canPurchase) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            IridiumSkyblock.getInstance().getShop().failSound.play(player);
            return;
        }

        if (shopItem.command == null) {
            // Add item to the player Inventory
            if (!InventoryUtils.hasEmptySlot(player.getInventory())) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inventoryFull.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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

        IridiumSkyblock.getInstance().getShop().successSound.play(player);

        player.sendMessage(
                StringUtils.color(
                        IridiumSkyblock.getInstance().getMessages().successfullyBought
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%amount%", String.valueOf(amount))
                                .replace("%item%", StringUtils.color(shopItem.name))
                                .replace("%vault_cost%", String.valueOf(vaultCost))
                                .replace("%crystal_cost%", String.valueOf(crystalCost))
                )
        );
    }

    /**
     * Sells an item for the Player in the shop.
     * He might not meet all requirements to do so.
     *
     * @param player   The player which wants to sell the item
     * @param shopItem The item which is to be sold
     * @param amount   The amount of the item which is to be sold
     */
    public void sell(Player player, ShopItem shopItem, int amount) {
        int inventoryAmount = InventoryUtils.getAmount(player.getInventory(), shopItem.type);
        if (inventoryAmount == 0) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSuchItem.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            IridiumSkyblock.getInstance().getShop().failSound.play(player);
            return;
        }

        int soldAmount = Math.min(inventoryAmount, amount);
        InventoryUtils.removeAmount(player.getInventory(), shopItem.type, soldAmount);
        giveReward(player, shopItem, soldAmount);
        IridiumSkyblock.getInstance().getShop().successSound.play(player);
    }

    public void giveReward(Player player, ShopItem item, int amount) {
        double vaultReward = calculateCost(amount, item.defaultAmount, item.sellReward.vault);
        int crystalReward = (int) calculateCost(amount, item.defaultAmount, item.sellReward.crystals);

        Island island = IridiumSkyblockAPI.getInstance().getUser(player).getIsland().get();
        IslandBank moneyIslandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().moneyBankItem);
        IslandBank crystalIslandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem);

        moneyIslandBank.setNumber(moneyIslandBank.getNumber() + vaultReward);
        crystalIslandBank.setNumber(crystalIslandBank.getNumber() + crystalReward);

        player.sendMessage(
                StringUtils.color(
                        IridiumSkyblock.getInstance().getMessages().successfullySold
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%amount%", String.valueOf(amount))
                                .replace("%item%", StringUtils.color(item.name))
                                .replace("%vault_reward%", String.valueOf(vaultReward))
                                .replace("%crystal_reward%", String.valueOf(crystalReward))
                )
        );
    }

    /**
     * Calculates the cost of an item with the provided amount given the default price and amount.
     *
     * @param amount        The amount which should be calculated
     * @param defaultAmount The default amount of the item
     * @param defaultPrice  The default price of the item
     * @return The price of the item in the given quantity
     */
    public double calculateCost(int amount, int defaultAmount, double defaultPrice) {
        double costPerItem = defaultPrice / defaultAmount;
        return Double.parseDouble(String.format("%.2f", costPerItem * amount));
    }

}
