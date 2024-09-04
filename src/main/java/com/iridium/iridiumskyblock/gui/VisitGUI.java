package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class VisitGUI extends PagedGUI<Island> {
    private final IridiumTeams<Island, User> iridiumTeams;

    public VisitGUI(Player player, IridiumTeams<Island, User> iridiumTeams) {
        super(
                1,
                IridiumSkyblock.getInstance().getInventories().visitGUI.size,
                IridiumSkyblock.getInstance().getInventories().visitGUI.background,
                iridiumTeams.getInventories().previousPage,
                iridiumTeams.getInventories().nextPage,
                player,
                iridiumTeams.getInventories().backButton
        );
        this.iridiumTeams = iridiumTeams;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().visitGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<Island> getPageObjects() {
        return iridiumTeams.getTeamManager().getTeams();
    }

    @Override
    public ItemStack getItemStack(Island island) {
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitGUI.item, iridiumTeams.getTeamsPlaceholderBuilder().getPlaceholders(island));
    }

    @Override
    public boolean isPaged() {
        return true;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);

        Island island = getItem(event.getSlot());
        if (island == null) return;

        IridiumSkyblock.getInstance().getCommandManager().executeCommand(event.getWhoClicked(), IridiumSkyblock.getInstance().getCommands().visitCommand, new String[]{island.getName()});
    }
}
