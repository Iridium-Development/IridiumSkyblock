package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.utils.SkinUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * GUI which allows users to manage invites.
 */
public class InvitesGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, String> invites;
    private final Inventory inventory;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public InvitesGUI(@NotNull Island island) {
        this.island = island;
        invites = new HashMap<>();
        inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.title));
        addContent(inventory);
    }

    /**
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(island);
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.background);

        int i = 0;
        for (IslandInvite islandInvite : islandInvites) {
            List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(islandInvite.getUser()).applyIslandPlaceholders(island).build();
            placeholderList.add(new Placeholder("inviter", islandInvite.getInviter().getName()));
            placeholderList.add(new Placeholder("time", islandInvite.getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
            if (IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item.material == XMaterial.PLAYER_HEAD
                    && IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item.headData == null) {
                Item initialItem = new Item(
                        XMaterial.PLAYER_HEAD,
                        i,
                        SkinUtils.getHeadData(islandInvite.getUser().getUuid()),
                        IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item.amount,
                        IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item.displayName,
                        IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item.lore
                );
                boolean querySkin = false;
                if (initialItem.headData == null) {
                    initialItem.headData = IridiumSkyblock.getInstance().getConfiguration().headDataLoadingSkin;
                    querySkin = true;
                }
                inventory.setItem(i, ItemStackUtils.makeItem(initialItem, placeholderList));
                final int slot = i;
                if (querySkin) Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                    String headData = SkinUtils.queryHeadData(islandInvite.getUser().getUuid());
                    if (headData == null) return;
                    initialItem.headData = headData;
                    ItemStack itemStackWithSkin = ItemStackUtils.makeItem(initialItem, placeholderList);
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> inventory.setItem(slot, itemStackWithSkin));
                });
            } else {
                inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, placeholderList));
            }
            invites.put(i, islandInvite.getUser().getName());
            i++;
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
        if (invites.containsKey(event.getSlot())) {
            String command = IridiumSkyblock.getInstance().getCommands().unInviteCommand.aliases.get(0);
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + invites.get(event.getSlot()));
            event.getWhoClicked().openInventory(getInventory());
        }
    }

}
