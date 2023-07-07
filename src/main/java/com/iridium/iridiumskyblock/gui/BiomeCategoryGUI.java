package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Biomes;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import com.iridium.iridiumteams.database.IridiumUser;
import com.iridium.iridiumteams.database.Team;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BiomeCategoryGUI<T extends Team, U extends IridiumUser<T>> extends BackGUI {
    private final IridiumSkyblock iridiumSkyblock;

    @Getter
    private final String categoryName;
    private final Biomes.BiomeCategory biomeCategory;

    public BiomeCategoryGUI(String categoryName, Inventory previousInventory, IridiumSkyblock iridiumSkyblock) {
        super(iridiumSkyblock.getInventories().biomeCategoryGUI.background,
                previousInventory,
                iridiumSkyblock.getInventories().backButton);
        this.iridiumSkyblock = iridiumSkyblock;
        this.categoryName = categoryName;
        this.biomeCategory = iridiumSkyblock.getBiomes().categories.get(categoryName);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI biomeCategoryGUI = iridiumSkyblock.getInventories().biomeCategoryGUI;
        Inventory inventory = Bukkit.createInventory(
                this,
                biomeCategoryGUI.size,
                StringUtils.color(biomeCategoryGUI.title.replace("%biome_category_name%", categoryName)));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        super.addContent(inventory);

        if (!iridiumSkyblock.getBiomes().items.containsKey(categoryName)) {
            iridiumSkyblock.getLogger().warning("Biome Category " + categoryName + " Is not configured with any items!");
            return;
        }
        for (Biomes.BiomeItem biomeItem : iridiumSkyblock.getBiomes().items.get(categoryName)) {
            ItemStack itemStack = biomeItem.type.parseItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemStack.setAmount(biomeItem.defaultAmount);
            itemMeta.setDisplayName(StringUtils.color(biomeItem.name));
            itemMeta.setLore(getBiomeLore(biomeItem));

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(biomeItem.slot, itemStack);
        }
    }

    private List<Placeholder> getBiomeLorePlaceholders(Biomes.BiomeItem item){
        List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                new Placeholder("amount", iridiumSkyblock.getBiomeManager().formatPrice(item.defaultAmount)),
                new Placeholder("vault_cost", iridiumSkyblock.getBiomeManager().formatPrice(item.buyCost.money))
        ));
        for (Map.Entry<String, Double> bankItem : item.buyCost.bankItems.entrySet()) {
            placeholders.add(new Placeholder(bankItem.getKey() + "_cost", iridiumSkyblock.getBiomeManager().formatPrice(bankItem.getValue())));
        }
        return placeholders;
    }

    private List<String> getBiomeLore(Biomes.BiomeItem item) {
        List<String> lore = item.lore == null ? new ArrayList<>() : new ArrayList<>(StringUtils.color(item.lore));
        List<Placeholder> placeholders = getBiomeLorePlaceholders(item);

        if (item.buyCost.canPurchase()) {
            lore.add(iridiumSkyblock.getBiomes().buyPriceLore);
        } else {
            lore.add(iridiumSkyblock.getBiomes().notPurchasableLore);
        }

        lore.addAll(iridiumSkyblock.getBiomes().biomeItemLore);

        return StringUtils.color(StringUtils.processMultiplePlaceholders(lore, placeholders));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
        Optional<Biomes.BiomeItem> biomeItem = iridiumSkyblock.getBiomes().items.get(categoryName).stream()
                .filter(item -> item.slot == event.getSlot())
                .findAny();

        if (!biomeItem.isPresent()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.isLeftClick() && biomeItem.get().buyCost.canPurchase()) {
            iridiumSkyblock.getBiomeManager().buy(player, biomeItem.get());
        } else {
            iridiumSkyblock.getBiomes().failSound.play(player);
        }
    }
}