package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which executes code upon confirmation.
 */
public class ConfirmationGUI extends GUI {

    private final @NotNull Runnable runnable;
    private final @NotNull CooldownProvider<CommandSender> cooldownProvider;
    private final boolean required;

    /**
     * The default constructor.
     *
     * @param runnable         The code that should be run when the user confirms their action
     * @param cooldownProvider The provider for cooldowns that should be started on success
     */
    public ConfirmationGUI(boolean required,@NotNull Runnable runnable, @NotNull CooldownProvider<CommandSender> cooldownProvider) {
        super(IridiumSkyblock.getInstance().getInventories().confirmationGUI);
        this.runnable = runnable;
        this.cooldownProvider = cooldownProvider;
        this.required = required;
    }

    public void open(Player player)
    {
        new BukkitRunnable() {
            public void run(){
                if (!required){
                    runnable.run();
                    cooldownProvider.applyCooldown(player);
                    player.closeInventory();
                }
                else
                {
                    player.openInventory(getInventory());
                }
            }
        }.runTaskLater(IridiumSkyblock.getInstance(), 1);
    }

    @Override
    public void addContent(Inventory inventory) {
        if(!required) return;
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.no.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.no));
        inventory.setItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.yes.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.yes));
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().confirmationGUI.no.slot) {
            player.closeInventory();
        } else if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().confirmationGUI.yes.slot) {
            runnable.run();
            player.closeInventory();
            if (cooldownProvider != null)
                cooldownProvider.applyCooldown(player);
        }
    }
}
