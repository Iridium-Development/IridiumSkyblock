package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IslandMissionsGUI extends PagedGUI<Map.Entry<String, Mission>> {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandMissionsGUI(@NotNull Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().missionsGUI.size,
                IridiumSkyblock.getInstance().getInventories().missionsGUI.background,
                IridiumSkyblock.getInstance().getInventories().previousPage,
                IridiumSkyblock.getInstance().getInventories().nextPage,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton);
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().missionsGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<Map.Entry<String, Mission>> getPageObjects() {
        List<Map.Entry<String, Mission>> missions = IridiumSkyblock.getInstance().getMissionsList().entrySet().stream()
                .filter(e -> e.getValue().getMissionType() == Mission.MissionType.ONCE).collect(Collectors.toList());

        switch (IridiumSkyblock.getInstance().getConfiguration().sortMissionsBy) {
            case KEY:
                missions.sort((a, b) -> {
                    return a.getKey().compareTo(b.getKey());
                });
                break;
            case DISPLAYNAME:
                missions.sort((a, b) -> {
                    if (a.getValue().getItem().displayName==null) return 1;
                    if (b.getValue().getItem().displayName==null) return -1;
                    return a.getValue().getItem().displayName.compareTo(b.getValue().getItem().displayName);
                });
                break;
            case SLOT:
                missions.sort((a, b) -> {
                    if (a.getValue().getItem().slot==null) return 1;
                    if (b.getValue().getItem().slot==null) return -1;
                    return a.getValue().getItem().slot.compareTo(b.getValue().getItem().slot);
                });
                break;
            default:
                break;
        }

        return missions;
    }

    @Override
    public ItemStack getItemStack(Map.Entry<String, Mission> entry) {
        List<Placeholder> placeholders = IntStream.range(0, entry.getValue().getMissions().size())
                .boxed()
                .map(integer -> IridiumSkyblock.getInstance().getIslandManager().getIslandMission(this.island,
                        entry.getValue(), entry.getKey(), integer))
                .map(islandMission -> new Placeholder("progress_" + (islandMission.getMissionIndex() + 1),
                        String.valueOf(islandMission.getProgress())))
                .collect(Collectors.toList());

        return ItemStackUtils.makeItem(entry.getValue().getItem(), placeholders);
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
    }

}
