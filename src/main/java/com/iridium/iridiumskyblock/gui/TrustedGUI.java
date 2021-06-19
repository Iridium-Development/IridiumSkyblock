package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.SingleItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.SkinUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * GUI which displays all trusted users of an Island.
 */
public class TrustedGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, User> members;
    private final Inventory inventory;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public TrustedGUI(@NotNull Island island) {
        this.island = island;
        this.members = new HashMap<>();
        SingleItemGUI singleItemGUI = IridiumSkyblock.getInstance().getInventories().trustedGUI;
        inventory = Bukkit.createInventory(this, singleItemGUI.size, StringUtils.color(singleItemGUI.title));
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
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().trustedGUI.background);

        int i = 0;
        List<IslandTrusted> islandTrustedList = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island);
        for (IslandTrusted islandTrusted : islandTrustedList) {
            List<Placeholder> placeholderList =
                    new PlaceholderBuilder().applyPlayerPlaceholders(islandTrusted.getUser()).applyIslandPlaceholders(island).build();
            placeholderList.add(new Placeholder("trustee", islandTrusted.getTruster().getName()));

            if (IridiumSkyblock.getInstance().getInventories().trustedGUI.item.material == XMaterial.PLAYER_HEAD
                    && IridiumSkyblock.getInstance().getInventories().trustedGUI.item.headData == null) {
                Item initialItem = new Item(
                        XMaterial.PLAYER_HEAD,
                        i,
                        SkinUtils.getHeadData(islandTrusted.getUser().getUuid()),
                        IridiumSkyblock.getInstance().getInventories().trustedGUI.item.amount,
                        IridiumSkyblock.getInstance().getInventories().trustedGUI.item.displayName,
                        IridiumSkyblock.getInstance().getInventories().trustedGUI.item.lore
                );
                boolean querySkin = false;
                if (initialItem.headData == null) {
                    initialItem.headData = IridiumSkyblock.getInstance().getConfiguration().headDataLoadingSkin;
                    querySkin = true;
                }
                inventory.setItem(i, ItemStackUtils.makeItem(initialItem, placeholderList));
                final int slot = i;
                if (querySkin) Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                    String headData = SkinUtils.queryHeadData(islandTrusted.getUser().getUuid());
                    if (headData == null) return;
                    initialItem.headData = headData;
                    ItemStack itemStackWithSkin = ItemStackUtils.makeItem(initialItem, placeholderList);
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> inventory.setItem(slot, itemStackWithSkin));
                });
            } else {
                inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().trustedGUI.item, placeholderList));
            }
            members.put(i, islandTrusted.getUser());
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
        if (members.containsKey(event.getSlot())) {
            User user = members.get(event.getSlot());
            String command = IridiumSkyblock.getInstance().getCommands().unTrustCommand.aliases.get(0);
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + user.getName());
            addContent(event.getInventory());
        }
    }

}
