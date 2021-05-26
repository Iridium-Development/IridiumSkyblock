package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.shop.ShopCategory;
import com.iridium.iridiumskyblock.shop.ShopItem;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ShopCategoryGUI implements GUI {

    private final ShopCategory category;

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
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getItemMeta() == null) {
            return;
        }

        Optional<ShopItem> clickedItem = category.items.stream()
            .filter(item -> item.slot == event.getSlot())
            .findAny();

        if (!clickedItem.isPresent()) {
            return;
        }

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

            itemMeta.setDisplayName(StringUtils.color(item.name));

            List<String> lore = item.lore == null ? new ArrayList<>() : new ArrayList<>(StringUtils.color(item.lore));

            if (item.isPurchasable()) {
                lore.add(
                    StringUtils.color(IridiumSkyblock.getInstance().getShop().buyPriceLore
                        .replace("%amount%", String.valueOf(item.defaultAmount))
                        .replace("%buy_price_vault%", String.valueOf(item.buyCost.vault))
                        .replace("%buy_price_crystals%", String.valueOf(item.buyCost.crystals))
                ));
            } else {
                lore.add(StringUtils.color(IridiumSkyblock.getInstance().getShop().notPurchasableLore));
            }

            if (item.isSellable()) {
                lore.add(
                    StringUtils.color(IridiumSkyblock.getInstance().getShop().sellRewardLore
                        .replace("%amount%", String.valueOf(item.defaultAmount))
                        .replace("%sell_reward_vault%", String.valueOf(item.sellReward.vault))
                        .replace("%sell_reward_crystals%", String.valueOf(item.sellReward.crystals))
                ));
            } else {
                lore.add(StringUtils.color(IridiumSkyblock.getInstance().getShop().notSellableLore));
            }

            IridiumSkyblock.getInstance().getShop().shopItemLore.stream()
                .map(StringUtils::color)
                .forEach(line -> lore.add(line.replace("%amount%", String.valueOf(item.defaultAmount))));

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(item.slot, itemStack);
        }
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
            IridiumSkyblock.getInstance().getShop().categorySize,
            StringUtils.color(
                IridiumSkyblock.getInstance().getShop().categoryTitle
                    .replace("%category_name%", category.name)
            )
        );

        addContent(inventory);

        return inventory;
    }

}
