package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.*;
import com.iridium.iridiumskyblock.configs.inventories.InventoryConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryConfigGUI extends GUI {

    private final InventoryConfig inventoryConfig;

    public InventoryConfigGUI(InventoryConfig inventoryConfig, Inventory previousInventory) {
        super(inventoryConfig, previousInventory);
        this.inventoryConfig = inventoryConfig;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, inventoryConfig.background);

        inventoryConfig.items.values().forEach(item -> inventory.setItem(item.slot, ItemStackUtils.makeItem(item)));


        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        for (String command : inventoryConfig.items.keySet()) {
            if (inventoryConfig.items.get(command).slot == event.getSlot()) {
                player.closeInventory();
                String[] arguments = command.split(" ");
                String commandExec = arguments[1];
                String[] args = new String[arguments.length - 1];
                System.arraycopy(arguments, 1, args, 0, arguments.length - 1);
                switch (commandExec) {
                    case "bank" -> BankCommand.bankExecutor(player, args);
                    case "boosters" -> BoosterCommand.boosterExecutor(player, args);
                    case "border" -> BorderCommand.borderExecutor(player, args);
                    case "delete" -> DeleteCommand.deleteExecutor(player, args);
                    case "home" -> HomeCommand.homeExecutor(player, args);
                    case "members" -> MembersCommand.membersExecutor(player, args);
                    case "missions" -> MissionCommand.missionExecutor(player, args);
                    case "permissions" -> PermissionsCommand.permissionsExecutor(player, args);
                    case "regen" -> RegenCommand.regenExecutor(player, args);
                    case "trusted" -> TrustCommand.trustExecutor(player, args);
                    case "upgrade" -> UpgradesCommand.upgradesExecutor(player, args);
                    case "warps" -> WarpsCommand.warpsExecutor(player, args);
                    default -> IridiumSkyblock.getInstance().getLogger().info(commandExec);
                }
            }
        }
    }

}
