package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.StackedBlock;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StackedBlockGUI implements GUI {

    private final StackedBlock block;


    public StackedBlockGUI(@NotNull StackedBlock block) {
        this.block = block;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == 13) {
            event.getWhoClicked().closeInventory();
            IridiumSkyblock.getInstance().getDatabaseManager().getStackedBlocksList().remove(block);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Invites"));
        ItemStack item = XMaterial.matchXMaterial(block.getMaterial()).orElse(XMaterial.BLACK_STAINED_GLASS_PANE).parseItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
        inventory.setItem(13, ItemStackUtils.makeItem(XMaterial.REDSTONE_BLOCK, Math.min(block.getAmountStacked(), 64), StringUtils.color("&aClick to redeem &cX"+ block.getAmountStacked()+" "+block.getMaterial())));
        return inventory;
    }
}
