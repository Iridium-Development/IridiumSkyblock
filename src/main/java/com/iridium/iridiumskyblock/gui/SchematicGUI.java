package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SchematicGUI implements GUI {

    private @NotNull IridiumSkyblock iridiumSkyblock;
    private @NotNull Player player;
    private @NotNull String islandName;
    private HashMap<Integer, Schematics.SchematicConfig> schematics = new HashMap<>();

    public SchematicGUI(@NotNull Player player, @NotNull String islandName, IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
        this.player = player;
        this.islandName = islandName;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Select a Schematic"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        for (Schematics.SchematicConfig schematicConfig : iridiumSkyblock.getSchematics().schematics) {
            inventory.setItem(schematicConfig.item.slot, ItemStackUtils.makeItem(schematicConfig.item));
            schematics.put(schematicConfig.item.slot, schematicConfig);
        }
        return inventory;
    }

    public void createIsland(Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland() != null) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().alreadyHaveIsland.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            return;
        }
        if (iridiumSkyblock.getDatabaseManager().getIslandByName(islandName).isPresent()) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().islandWithNameAlreadyExists.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            return;
        }
        player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().creatingIsland.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
        iridiumSkyblock.getIslandManager().createIsland(player, islandName, schematicConfig).thenAccept(island -> player.teleport(island.getHome()));
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (schematics.containsKey(event.getSlot())) {
            createIsland(schematics.get(event.getSlot()));
            event.getWhoClicked().closeInventory();
        }
    }
}
