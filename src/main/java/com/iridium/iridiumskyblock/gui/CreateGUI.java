package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.ClosableGUI;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.concurrent.CompletableFuture;

@Getter
public class CreateGUI extends SchematicGUI implements ClosableGUI {
    private final CompletableFuture<String> completableFuture;

    public CreateGUI(Player player, CompletableFuture<String> completableFuture) {
        super(player);
        this.completableFuture = completableFuture;
    }

    @Override
    public void selectSchematic(String schematic) {
        completableFuture.complete(schematic);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        completableFuture.complete(null);
    }
}
