package com.iridium.iridiumskyblock.biomes;

import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Biomes.BiomeCategoryConfig;
import com.iridium.iridiumskyblock.biomes.BiomeItem.BuyCost;
import com.iridium.iridiumskyblock.api.BiomePurchaseEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Handles Biomes selection and purchase.
 */
public class BiomesManager {

    private final IridiumSkyblock plugin = IridiumSkyblock.getInstance();

    private final List<BiomeCategory> categories = new ArrayList<>();

    public void reloadCategories() {
        categories.clear();

        for (String categoryName : IridiumSkyblock.getInstance().getBiomes().biomes.keySet()) {
            BiomeCategoryConfig biomeCategoryConfig = IridiumSkyblock.getInstance().getBiomes().categories.get(categoryName);
            if (biomeCategoryConfig == null) {
                IridiumSkyblock.getInstance().getLogger().warning("Biome category " + categoryName + " is not configured, skipping...");
                continue;
            }

            categories.add(
                    new BiomeCategory(
                            categoryName,
                            biomeCategoryConfig.item,
                            IridiumSkyblock.getInstance().getBiomes().biomes.get(categoryName),
                            biomeCategoryConfig.inventoryRows * 9
                    )
            );
        }
    }

    /**
     * Returns a list of all loaded categories.
     *
     * @return All loaded categories for Biomes
     */
    public List<BiomeCategory> getCategories() {
        return categories;
    }

    /**
     * Returns the category with the provided name, null if there is none.
     *
     * @param name The name of the category
     * @return The category with the name
     */
    public Optional<BiomeCategory> getCategoryByName(String name) {
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
    public Optional<BiomeCategory> getCategoryBySlot(int slot) {
        return categories.stream()
                .filter(category -> category.item.slot == slot)
                .findAny();
    }

    /**
     * Buys a biome to apply to a player's island
     * He might not have enough money to do so.
     *
     * @param player   The player which wants to buy the item
     * @param biomeItem The Biome listing and its associated biome
     * @param amount   The amount of the item which is requested
     */
    public void buy(Player player, BiomeItem biomeItem, int amount) {
        BuyCost buyCost = BiomeItem.buyCost;
        double vaultCost = calculateCost(amount, biomeItem.defaultAmount, buyCost.vault);
        int crystalCost = (int) calculateCost(amount, biomeItem.defaultAmount, buyCost.crystals);
        final Optional<Island> island = IridiumSkyblockAPI.getInstance().getUser(player).getIsland();
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        boolean canPurchase = PlayerUtils.canPurchase(player, island.get(), crystalCost, vaultCost);
        if (!canPurchase) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotAfford.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            IridiumSkyblock.getInstance().getBiomes().failSound.play(player);
            return;
        }

        BiomePurchaseEvent biomePurchaseEvent = new BiomePurchaseEvent(player, biomeItem, amount);
        Bukkit.getPluginManager().callEvent(biomePurchaseEvent);
        if (biomePurchaseEvent.isCancelled()) return;

        final User user = this.plugin.getUserManager().getUser(player);
        final Optional<Island> islandOptional = user.getIsland();
        final Optional<XBiome> biomeOptional = XBiome.matchXBiome(biomeItem.toString());
        IridiumSkyblock.getInstance().getIslandManager().setIslandBiome(islandOptional.get(), biomeOptional.get());
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().changedBiome
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%biome%", WordUtils.capitalizeFully(biomeOptional.get().name().toLowerCase().replace("_", " ")))));

        // Only run the withdrawing function when the user can buy it.
        PlayerUtils.pay(player, island.get(), crystalCost, vaultCost);

        IridiumSkyblock.getInstance().getBiomes().successSound.play(player);

        player.sendMessage(
                StringUtils.color(
                        IridiumSkyblock.getInstance().getMessages().successfullyBought
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%amount%", String.valueOf(amount))
                                .replace("%item%", StringUtils.color(biomeItem.name))
                                .replace("%vault_cost%", String.valueOf(vaultCost))
                                .replace("%crystal_cost%", String.valueOf(crystalCost))
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
    private double calculateCost(int amount, int defaultAmount, double defaultPrice) {
        double costPerItem = defaultPrice / defaultAmount;
        return round(costPerItem * amount, 2);
    }

    /**
     * Rounds a double with the specified amount of decimal places.
     *
     * @param value  The value of the double that should be rounded
     * @param places The amount of decimal places
     * @return The rounded double
     */
    private double round(double value, int places) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

}
