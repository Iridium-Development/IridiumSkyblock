package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.LogInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * GUI which allows users to manage the Island bank.
 */
public class LogsGUI extends GUI {

    private int membersPage = 1;
    private int invitesPage = 1;
    private int trustsPage = 1;
    private int bankPage = 1;
    private int boostersPage = 1;
    private int upgradesPage = 1;
    private int rewardsPage = 1;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public LogsGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().logsGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        LogInventoryConfig logInventoryConfig = IridiumSkyblock.getInstance().getInventories().logsGUI;
        inventory.clear();

        InventoryUtils.fillInventory(inventory, logInventoryConfig.background);

        setItemStack(inventory, logInventoryConfig.IslandMembers, membersPage, LogAction.USER_JOINED, LogAction.USER_KICKED, LogAction.USER_LEFT, LogAction.USER_DEMOTED, LogAction.USER_PROMOTED);
        setItemStack(inventory, logInventoryConfig.IslandInvites, invitesPage, LogAction.USER_INVITED, LogAction.USER_UNINVITED);
        setItemStack(inventory, logInventoryConfig.IslandTrusts, trustsPage, LogAction.USER_TRUSTED, LogAction.USER_UNTRUSTED);
        setItemStack(inventory, logInventoryConfig.IslandBank, bankPage, LogAction.BANK_DEPOSIT, LogAction.BANK_WITHDRAW);
        setItemStack(inventory, logInventoryConfig.IslandBoosters, boostersPage, LogAction.BOOSTER_PURCHASE);
        setItemStack(inventory, logInventoryConfig.IslandUpgrades, upgradesPage, LogAction.UPGRADE_PURCHASE);
        setItemStack(inventory, logInventoryConfig.IslandRewards, rewardsPage, LogAction.REWARD_REDEEMED);
    }

    public void setItemStack(Inventory inventory, Item item, int page, LogAction... logActions) {
        ItemStack itemStack = ItemStackUtils.makeItem(item, new PlaceholderBuilder().applyIslandPlaceholders(getIsland()).build());
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();

        List<IslandLog> islandLogs = IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().getEntries(getIsland()).stream()
                .filter(islandLog -> Arrays.stream(logActions).anyMatch(logAction -> logAction.equals(islandLog.getLogAction())))
                .sorted(Comparator.comparing(IslandLog::getTime).reversed())
                .collect(Collectors.toList());

        int index = 0;
        for (IslandLog islandLog : islandLogs) {
            if ((page - 1) * 10 <= index && page * 10 > index) {
                long time = (System.currentTimeMillis() - islandLog.getTime()) / 1000L;
                int days = (int) TimeUnit.SECONDS.toDays(time);
                int hours = (int) Math.floor(TimeUnit.SECONDS.toHours(time - days * 86400L));
                int minutes = (int) Math.floor((time - (days * 86400) - (hours * 3600)) / 60.0D);
                int seconds = (int) Math.floor((time - (days * 86400) - (hours * 3600)) % 60.0D);

                lore.add(StringUtils.color(getLore(islandLog.getLogAction())
                        .replace("%type%", islandLog.getData())
                        .replace("%amount%", String.valueOf(islandLog.getAmount()))
                        .replace("%user%", islandLog.getUser().getName()))
                        .replace("%target%", islandLog.getTarget().getName())
                        .replace("%seconds%", String.valueOf(seconds))
                        .replace("%minutes%", String.valueOf(minutes))
                        .replace("%hours%", String.valueOf(hours))
                        .replace("%days%", String.valueOf(days)));
            }
            index++;
        }

        if (itemMeta.getLore() != null) {
            lore.addAll(itemMeta.getLore());
        }

        int maxPage = (int) Math.ceil(islandLogs.size() / 10.00);

        itemMeta.setLore(lore.stream().map(s -> s
                        .replace("%current_page%", String.valueOf(page))
                        .replace("%max_page%", String.valueOf(maxPage > 0 ? maxPage : 1))
                ).collect(Collectors.toList())
        );
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(item.slot, itemStack);
    }

    public String getLore(LogAction logAction) {
        LogInventoryConfig logInventoryConfig = IridiumSkyblock.getInstance().getInventories().logsGUI;
        switch (logAction) {
            case USER_JOINED:
                return logInventoryConfig.USER_JOINED;
            case USER_KICKED:
                return logInventoryConfig.USER_KICKED;
            case USER_LEFT:
                return logInventoryConfig.USER_LEFT;
            case USER_DEMOTED:
                return logInventoryConfig.USER_DEMOTED;
            case USER_PROMOTED:
                return logInventoryConfig.USER_PROMOTED;
            case USER_INVITED:
                return logInventoryConfig.USER_INVITED;
            case USER_UNINVITED:
                return logInventoryConfig.USER_UNINVITED;
            case USER_TRUSTED:
                return logInventoryConfig.USER_TRUSTED;
            case USER_UNTRUSTED:
                return logInventoryConfig.USER_UNTRUSTED;
            case BANK_DEPOSIT:
                return logInventoryConfig.BANK_DEPOSIT;
            case BANK_WITHDRAW:
                return logInventoryConfig.BANK_WITHDRAW;
            case BOOSTER_PURCHASE:
                return logInventoryConfig.BOOSTER_PURCHASE;
            case UPGRADE_PURCHASE:
                return logInventoryConfig.UPGRADE_PURCHASE;
            case REWARD_REDEEMED:
                return logInventoryConfig.REWARD_REDEEMED;
            default:
                return "";
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getClick().equals(ClickType.LEFT) && !event.getClick().equals(ClickType.RIGHT)) return;
        int i = event.getClick().equals(ClickType.LEFT) ? -1 : 1;
        LogInventoryConfig logInventoryConfig = IridiumSkyblock.getInstance().getInventories().logsGUI;
        if (event.getSlot() == logInventoryConfig.IslandMembers.slot) {
            if (canChangePage(membersPage, i, LogAction.USER_JOINED, LogAction.USER_KICKED, LogAction.USER_LEFT, LogAction.USER_DEMOTED, LogAction.USER_PROMOTED)) {
                membersPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandTrusts.slot) {
            if (canChangePage(trustsPage, i, LogAction.USER_TRUSTED, LogAction.USER_UNTRUSTED)) {
                trustsPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandInvites.slot) {
            if (canChangePage(invitesPage, i, LogAction.USER_INVITED, LogAction.USER_UNINVITED)) {
                invitesPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandBoosters.slot) {
            if (canChangePage(boostersPage, i, LogAction.BOOSTER_PURCHASE)) {
                boostersPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandBank.slot) {
            if (canChangePage(bankPage, i, LogAction.BANK_DEPOSIT, LogAction.BANK_WITHDRAW)) {
                bankPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandUpgrades.slot) {
            if (canChangePage(upgradesPage, i, LogAction.UPGRADE_PURCHASE)) {
                upgradesPage += i;
            }
        } else if (event.getSlot() == logInventoryConfig.IslandRewards.slot) {
            if (canChangePage(rewardsPage, i, LogAction.REWARD_REDEEMED)) {
                rewardsPage += i;
            }
        }
        addContent(event.getInventory());
    }

    private boolean canChangePage(int page, int change, LogAction... logActions) {
        List<IslandLog> islandLogs = IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().getEntries(getIsland()).stream()
                .filter(islandLog -> Arrays.stream(logActions).anyMatch(logAction -> logAction.equals(islandLog.getLogAction())))
                .sorted(Comparator.comparing(IslandLog::getTime).reversed())
                .collect(Collectors.toList());
        int maxPage = (int) Math.ceil(islandLogs.size() / 10.00);

        return page + change > 0 && page + change <= maxPage;
    }


}
