package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.shop.ShopCategory;
import com.iridium.iridiumskyblock.shop.ShopItem;
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
 * GUI which shows all items in a {@link ShopCategory} and allows players to purchase them.
 */
public class ShopCategoryGUI implements GUI {

    private final ShopCategory category;

    /**
     * The default constructor.
     *
     * @param category The category whose items should be displayed in this GUI
     */
    public ShopCategoryGUI(ShopCategory category) {
        this.category = category;
    }

    /**
     * Called when there is a click in this GUI. Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<ShopItem> clickedItem = category.items.stream()
            .filter(item -> item.slot == event.getSlot())
            .findAny();

        if (!clickedItem.isPresent()) {
            return;
        }

        // Perform the action corresponding to the click
        Player player = (Player) event.getWhoClicked();
        ShopItem shopItem = clickedItem.get();
        if (event.isLeftClick() && shopItem.isPurchasable()) {
            if (event.isShiftClick()) {
                IridiumSkyblock.getInstance().getShopManager().buy(player, shopItem, 64);
            } else {
                IridiumSkyblock.getInstance().getShopManager().buy(player, shopItem, shopItem.defaultAmount);
            }
        } else if (shopItem.isSellable()) {
            if (event.isShiftClick()) {
                IridiumSkyblock.getInstance().getShopManager().sell(player, shopItem, 64);
            } else {
                IridiumSkyblock.getInstance().getShopManager().sell(player, shopItem, shopItem.defaultAmount);
            }
        } else {
            IridiumSkyblock.getInstance().getShop().failSound.play(player);
        }
    }

    /**
     * Called when updating the Inventories contents
     */
    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getShop().categoryBackground);

        for (ShopItem item : category.items) {
            ItemStack itemStack = item.type.parseItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemStack.setAmount(item.defaultAmount);
            itemMeta.setDisplayName(StringUtils.color(item.name));

            List<String> lore = item.lore == null ? new ArrayList<>() : new ArrayList<>(StringUtils.color(item.lore));
            addShopLore(lore, item);

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(item.slot, itemStack);
        }
    }

    private void addShopLore(List<String> lore, ShopItem item) {
        if (item.isPurchasable()) {
            lore.add(
                StringUtils.color(IridiumSkyblock.getInstance().getShop().buyPriceLore
                    .replace("%amount%", String.valueOf(item.defaultAmount))
                    .replace("%buy_price_vault%", String.valueOf(item.buyCost.vault))
                    .replace("%buy_price_crystals%", String.valueOf(item.buyCost.crystals))
                )
            );
        } else {
            lore.add(StringUtils.color(IridiumSkyblock.getInstance().getShop().notPurchasableLore));
        }

        if (item.isSellable()) {
            lore.add(
                StringUtils.color(IridiumSkyblock.getInstance().getShop().sellRewardLore
                    .replace("%amount%", String.valueOf(item.defaultAmount))
                    .replace("%sell_reward_vault%", String.valueOf(item.sellReward.vault))
                    .replace("%sell_reward_crystals%", String.valueOf(item.sellReward.crystals))
                )
            );
        } else {
            lore.add(StringUtils.color(IridiumSkyblock.getInstance().getShop().notSellableLore));
        }

        IridiumSkyblock.getInstance().getShop().shopItemLore.stream()
            .map(StringUtils::color)
            .forEach(line -> lore.add(
                line.replace("%amount%", String.valueOf(item.defaultAmount))
            ));
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(
            this,
            category.size,
            StringUtils.color(
                IridiumSkyblock.getInstance().getShop().categoryTitle
                    .replace("%category_name%", category.name)
            )
        );

        addContent(inventory);

        return inventory;
    }

}
