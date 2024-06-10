package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.configs.Shop;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import com.iridium.iridiumteams.database.TeamBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.addAll;

public abstract class SchematicGUI extends BackGUI {

    public SchematicGUI(Inventory previousInventory) {
        super(
                IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.background,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().islandSchematicGUI;
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        super.addContent(inventory);

        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {

            ItemStack itemStack = ItemStackUtils.makeItem(entry.getValue().item);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta != null) {
                itemMeta.setLore(getSchematicLore(entry.getValue()));

                itemStack.setItemMeta(itemMeta);
            }

            inventory.setItem(entry.getValue().item.slot, itemStack);
        }
    }

    private List<String> getSchematicLore(Schematics.SchematicConfig item) {
        List<Placeholder> placeholders = getSchematicLorePlaceholders(item);

        List<String> lore = new ArrayList<>(item.item.lore);

        if (item.regenCost.canPurchase()) {
            lore.add(String.valueOf(IridiumSkyblock.getInstance().getSchematics().buyPriceLore));
        }

        if (item.minLevel > 1) {
            lore.add(IridiumSkyblock.getInstance().getSchematics().levelRequirementLore);
        }

        if (!IridiumSkyblock.getInstance().getConfiguration().islandCreationCost) {
            lore.add(IridiumSkyblock.getInstance().getSchematics().regenDisclaimer);
        }

        return StringUtils.color(StringUtils.processMultiplePlaceholders(lore, placeholders));
    }

    private List<Placeholder> getSchematicLorePlaceholders(Schematics.SchematicConfig item) {
        List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                new Placeholder("vault_cost", IridiumSkyblock.getInstance().getSchematicManager().formatPrice(item.regenCost.money)),
                new Placeholder("level", String.valueOf(item.minLevel))
        ));

        for (Map.Entry<String, Double> bankItem : item.regenCost.bankItems.entrySet()) {
            placeholders.add(new Placeholder(bankItem.getKey() + "_cost", IridiumSkyblock.getInstance().getSchematicManager().formatPrice(bankItem.getValue())));
        }

        return placeholders;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);

        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
            if (entry.getValue().item.slot != event.getSlot()) continue;
            selectSchematic(entry.getKey());
            event.getWhoClicked().closeInventory();
        }
    }

    public abstract void selectSchematic(String schematic);

}
