package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.database.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderBuilder {
    private final List<Placeholder> placeholderList = new ArrayList<>();

    public PlaceholderBuilder() {
        placeholderList.add(new Placeholder("prefix", IridiumSkyblock.getInstance().getConfiguration().prefix));
    }

    public PlaceholderBuilder applyIslandPlaceholders(Island island) {
        IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island, "member");
        int memberLimit = IridiumSkyblock.getInstance().getUpgrades().memberUpgrade.upgrades.get(islandUpgrade.getLevel()).amount;
        placeholderList.add(new Placeholder("island_name", island.getName()));
        placeholderList.add(new Placeholder("island_owner", island.getOwner().getName()));
        placeholderList.add(new Placeholder("island_value", IridiumSkyblock.getInstance().getNumberFormatter().format(island.getValue())));
        placeholderList.add(new Placeholder("island_rank", IridiumSkyblock.getInstance().getNumberFormatter().format(island.getRank())));
        placeholderList.add(new Placeholder("island_members", IridiumSkyblock.getInstance().getNumberFormatter().format(island.getMembers().size())));
        placeholderList.add(new Placeholder("island_members_limit", IridiumSkyblock.getInstance().getNumberFormatter().format(memberLimit)));
        placeholderList.add(new Placeholder("island_level", IridiumSkyblock.getInstance().getNumberFormatter().format(island.getLevel())));
        placeholderList.add(new Placeholder("island_create", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));

        IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet().stream().map(material -> new Placeholder(material.name() + "_AMOUNT", IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getIslandManager().getIslandBlockAmount(island, material)))).forEach(placeholderList::add);
        return this;
    }

    public PlaceholderBuilder applyPlayerPlaceholders(User user) {
        placeholderList.add(new Placeholder("player_name", user.getName()));
        placeholderList.add(new Placeholder("has_island", user.getIsland().isPresent() ? IridiumSkyblock.getInstance().getMessages().yes : IridiumSkyblock.getInstance().getMessages().no));
        placeholderList.add(new Placeholder("player_rank", user.getIslandRank().getDisplayName()));
        placeholderList.add(new Placeholder("player_join", user.getJoinTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
        return this;
    }

    public List<Placeholder> build() {
        return placeholderList;
    }
}
