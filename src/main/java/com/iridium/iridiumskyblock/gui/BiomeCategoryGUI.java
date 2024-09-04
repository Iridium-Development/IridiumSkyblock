package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BiomeCategoryGUI extends BackGUI {

    @Getter
    private final String categoryName;
    private final Biomes.BiomeCategory biomeCategory;

    public BiomeCategoryGUI(String categoryName, Player player) {
        super(IridiumSkyblock.getInstance().getInventories().biomeCategoryGUI.background, player, IridiumSkyblock.getInstance().getInventories().backButton);
        this.categoryName = categoryName;
        this.biomeCategory = IridiumSkyblock.getInstance().getBiomes().categories.get(categoryName);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI biomeCategoryGUI = IridiumSkyblock.getInstance().getInventories().biomeCategoryGUI;
        Inventory inventory = Bukkit.createInventory(this, biomeCategory.inventorySize, StringUtils.color(biomeCategoryGUI.title.replace("%biome_category_name%", categoryName)));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        super.addContent(inventory);

        if (!IridiumSkyblock.getInstance().getBiomes().items.containsKey(categoryName)) {
            IridiumSkyblock.getInstance().getLogger().warning("Biome Category " + categoryName + " Is not configured with any items!");
            return;
        }
        for (Biomes.BiomeItem biomeItem : IridiumSkyblock.getInstance().getBiomes().items.get(categoryName)) {
            ItemStack itemStack = biomeItem.type.parseItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemStack.setAmount(biomeItem.defaultAmount);
            itemMeta.setDisplayName(StringUtils.color(biomeItem.name));
            itemMeta.setLore(getBiomeLore(biomeItem));

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(biomeItem.slot, itemStack);
        }
    }

    private List<Placeholder> getBiomeLorePlaceholders(Biomes.BiomeItem item) {
        List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                new Placeholder("minLevel", String.valueOf(item.minLevel)),
                new Placeholder("vault_cost", IridiumSkyblock.getInstance().getBiomeManager().formatPrice(item.buyCost.money))
        ));
        for (Map.Entry<String, Double> bankItem : item.buyCost.bankItems.entrySet()) {
            placeholders.add(new Placeholder(bankItem.getKey() + "_cost", IridiumSkyblock.getInstance().getBiomeManager().formatPrice(bankItem.getValue())));
        }
        return placeholders;
    }

    private List<String> getBiomeLore(Biomes.BiomeItem item) {
        List<String> lore = item.lore == null ? new ArrayList<>() : new ArrayList<>(StringUtils.color(item.lore));
        List<Placeholder> placeholders = getBiomeLorePlaceholders(item);

        if (item.buyCost.canPurchase()) {
            lore.add(IridiumSkyblock.getInstance().getBiomes().buyPriceLore);
        } else {
            lore.add(IridiumSkyblock.getInstance().getBiomes().notPurchasableLore);
        }

        if(item.minLevel > 1){
            lore.add(IridiumSkyblock.getInstance().getBiomes().levelRequirementLore);
        }

        lore.addAll(IridiumSkyblock.getInstance().getBiomes().biomeItemLore);

        return StringUtils.color(StringUtils.processMultiplePlaceholders(lore, placeholders));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
        Optional<Biomes.BiomeItem> biomeItem = IridiumSkyblock.getInstance().getBiomes().items.get(categoryName).stream()
                .filter(item -> item.slot == event.getSlot())
                .findAny();

        if (!biomeItem.isPresent()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.isLeftClick() && biomeItem.get().buyCost.canPurchase()) {
            IridiumSkyblock.getInstance().getBiomeManager().buy(player, biomeItem.get());
            player.closeInventory();
        } else {
            IridiumSkyblock.getInstance().getBiomes().failSound.play(player);
        }
    }
}