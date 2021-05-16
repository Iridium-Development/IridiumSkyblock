package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.LogInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * GUI which allows users to manage the Island bank.
 */
public class LogsGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public LogsGUI(@NotNull Island island) {
        this.island = island;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().logsGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().logsGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        LogInventoryConfig logInventoryConfig = IridiumSkyblock.getInstance().getInventories().logsGUI;
        inventory.clear();

        InventoryUtils.fillInventory(inventory, logInventoryConfig.background);

        List<IslandLog> islandLogs = IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().getEntries(island);
        islandLogs.sort(Comparator.comparing(IslandLog::getTime).reversed());

        ItemStack islandMembers = ItemStackUtils.makeItem(logInventoryConfig.IslandMembers, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandMembersLore = new ArrayList<>();

        ItemStack islandInvites = ItemStackUtils.makeItem(logInventoryConfig.IslandInvites, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandInvitesLore = new ArrayList<>();

        ItemStack islandTrusts = ItemStackUtils.makeItem(logInventoryConfig.IslandTrusts, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandTrustsLore = new ArrayList<>();

        ItemStack islandBank = ItemStackUtils.makeItem(logInventoryConfig.IslandBank, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandBankLore = new ArrayList<>();

        ItemStack islandBooster = ItemStackUtils.makeItem(logInventoryConfig.IslandBoosters, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandBoosterLore = new ArrayList<>();

        ItemStack islandUpgrade = ItemStackUtils.makeItem(logInventoryConfig.IslandUpgrades, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandUpgradeLore = new ArrayList<>();

        ItemStack islandRewards = ItemStackUtils.makeItem(logInventoryConfig.IslandRewards, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        ArrayList<String> islandRewardsLore = new ArrayList<>();

        for (IslandLog islandLog : islandLogs) {

            long time = (System.currentTimeMillis() - islandLog.getTime()) / 1000L;
            int days = (int) TimeUnit.SECONDS.toDays(time);
            int hours = (int) Math.floor(TimeUnit.SECONDS.toHours(time - days * 86400L));
            int minutes = (int) Math.floor((time - (days * 86400) - (hours * 3600)) / 60.0D);
            int seconds = (int) Math.floor((time - (days * 86400) - (hours * 3600)) % 60.0D);
            switch (islandLog.getLogAction()) {
                case USER_JOINED:
                    islandMembersLore.add(StringUtils.color(logInventoryConfig.USER_JOINED
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_KICKED:
                    islandMembersLore.add(StringUtils.color(logInventoryConfig.USER_KICKED
                            .replace("%user%", islandLog.getUser().getName())
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days))));
                    break;
                case USER_LEFT:
                    islandMembersLore.add(StringUtils.color(logInventoryConfig.USER_LEFT
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_DEMOTED:
                    islandMembersLore.add(StringUtils.color(logInventoryConfig.USER_DEMOTED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_PROMOTED:
                    islandMembersLore.add(StringUtils.color(logInventoryConfig.USER_PROMOTED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_INVITED:
                    islandInvitesLore.add(StringUtils.color(logInventoryConfig.USER_INVITED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_UNINVITED:
                    islandInvitesLore.add(StringUtils.color(logInventoryConfig.USER_UNINVITED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_TRUSTED:
                    islandTrustsLore.add(StringUtils.color(logInventoryConfig.USER_TRUSTED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case USER_UNTRUSTED:
                    islandTrustsLore.add(StringUtils.color(logInventoryConfig.USER_UNTRUSTED
                            .replace("%target%", islandLog.getTarget().getName())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case BANK_DEPOSIT:
                    islandBankLore.add(StringUtils.color(logInventoryConfig.BANK_DEPOSIT
                            .replace("%amount%", String.valueOf(islandLog.getAmount()))
                            .replace("%type%", islandLog.getData())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case BANK_WITHDRAW:
                    islandBankLore.add(StringUtils.color(logInventoryConfig.BANK_WITHDRAW
                            .replace("%amount%", String.valueOf(islandLog.getAmount()))
                            .replace("%type%", islandLog.getData())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case BOOSTER_PURCHASE:
                    islandBoosterLore.add(StringUtils.color(logInventoryConfig.BOOSTER_PURCHASE.
                            replace("%type%", islandLog.getData())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case UPGRADE_PURCHASE:
                    islandUpgradeLore.add(StringUtils.color(logInventoryConfig.UPGRADE_PURCHASE
                            .replace("%type%", islandLog.getData())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;
                case REWARD_REDEEMED:
                    islandRewardsLore.add(StringUtils.color(logInventoryConfig.REWARD_REDEEMED
                            .replace("%type%", islandLog.getData())
                            .replace("%user%", islandLog.getUser().getName()))
                            .replace("%seconds%", String.valueOf(seconds))
                            .replace("%minutes%", String.valueOf(minutes))
                            .replace("%hours%", String.valueOf(hours))
                            .replace("%days%", String.valueOf(days)));
                    break;

            }
        }

        inventory.setItem(logInventoryConfig.IslandMembers.slot, setLore(islandMembers, islandMembersLore));
        inventory.setItem(logInventoryConfig.IslandInvites.slot, setLore(islandInvites, islandInvitesLore));
        inventory.setItem(logInventoryConfig.IslandTrusts.slot, setLore(islandTrusts, islandTrustsLore));
        inventory.setItem(logInventoryConfig.IslandBank.slot, setLore(islandBank, islandBankLore));
        inventory.setItem(logInventoryConfig.IslandBoosters.slot, setLore(islandBooster, islandBoosterLore));
        inventory.setItem(logInventoryConfig.IslandUpgrades.slot, setLore(islandUpgrade, islandUpgradeLore));
        inventory.setItem(logInventoryConfig.IslandRewards.slot, setLore(islandRewards, islandRewardsLore));
    }

    public ItemStack setLore(ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
    }


}
