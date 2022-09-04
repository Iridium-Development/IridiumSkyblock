package com.iridium.iridiumskyblock.gui;

import lombok.Getter;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.CompletableFuture;

@Getter
public class CreateGUI extends SchematicGUI {
    private final CompletableFuture<String> completableFuture;

    public CreateGUI(Inventory previousInventory, CompletableFuture<String> completableFuture) {
        super(previousInventory);
        this.completableFuture = completableFuture;
    }

    @Override
    public void selectSchematic(String schematic) {
        completableFuture.complete(schematic);
    }

}
