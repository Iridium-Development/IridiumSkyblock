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
 * GUI which displays all members of an Island and allows quick rank management.
 */
public class MembersGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, User> members;
    private final Inventory inventory;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public MembersGUI(@NotNull Island island) {
        this.island = island;
        this.members = new HashMap<>();
        SingleItemGUI singleItemGUI = IridiumSkyblock.getInstance().getInventories().membersGUI;
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
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().membersGUI.background);

        int i = 0;
        for (User member : island.getMembers()) {
            if (IridiumSkyblock.getInstance().getInventories().membersGUI.item.material == XMaterial.PLAYER_HEAD
                    && IridiumSkyblock.getInstance().getInventories().membersGUI.item.headData == null) {
                Item initialItem = new Item(
                        XMaterial.PLAYER_HEAD,
                        i,
                        SkinUtils.getHeadData(member.getUuid()),
                        IridiumSkyblock.getInstance().getInventories().membersGUI.item.amount,
                        IridiumSkyblock.getInstance().getInventories().membersGUI.item.displayName,
                        IridiumSkyblock.getInstance().getInventories().membersGUI.item.lore
                );
                List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(member).applyIslandPlaceholders(island).build();
                boolean querySkin = false;
                if (initialItem.headData == null) {
                    initialItem.headData = IridiumSkyblock.getInstance().getConfiguration().headDataLoadingSkin;
                    querySkin = true;
                }
                inventory.setItem(i, ItemStackUtils.makeItem(initialItem, placeholderList));
                final int slot = i;
                if (querySkin) Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                    String headData = SkinUtils.queryHeadData(member.getUuid());
                    if (headData == null) return;
                    initialItem.headData = headData;
                    ItemStack itemStackWithSkin = ItemStackUtils.makeItem(initialItem, placeholderList);
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> inventory.setItem(slot, itemStackWithSkin));
                });
            } else {
                inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().membersGUI.item, new PlaceholderBuilder().applyPlayerPlaceholders(member).applyIslandPlaceholders(island).build()));
            }
            members.put(i, member);
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

            String command = null;
            switch (event.getClick()) {
                case LEFT:
                    command = IridiumSkyblock.getInstance().getCommands().demoteCommand.aliases.get(0);
                    break;
                case RIGHT:
                    command = IridiumSkyblock.getInstance().getCommands().promoteCommand.aliases.get(0);
            }
            if (command != null)
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + user.getName());
            addContent(event.getInventory());
        }
    }

}
