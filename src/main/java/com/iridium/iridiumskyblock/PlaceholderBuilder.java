package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.Placeholder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderBuilder {
    private final List<Placeholder> placeholderList = new ArrayList<>();

    public PlaceholderBuilder() {
        placeholderList.add(new Placeholder("prefix", IridiumSkyblock.getInstance().getConfiguration().prefix));
    }

    public PlaceholderBuilder applyIslandPlaceholders(Island island) {
        placeholderList.add(new Placeholder("island_name", island.getName()));
        placeholderList.add(new Placeholder("island_owner", island.getOwner().getName()));
        placeholderList.add(new Placeholder("island_value", String.valueOf(island.getValue())));
        placeholderList.add(new Placeholder("island_rank", String.valueOf(island.getRank())));
        placeholderList.add(new Placeholder("island_level", String.valueOf(island.getLevel())));
        placeholderList.add(new Placeholder("island_create", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));

        IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet().stream().map(material -> new Placeholder(material.name() + "_AMOUNT", String.valueOf(IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material).map(IslandBlocks::getAmount).orElse(0)))).forEach(placeholderList::add);
        return this;
    }

    public PlaceholderBuilder applyPlayerPlaceholders(User user) {
        placeholderList.add(new Placeholder("player_name", user.getName()));
        placeholderList.add(new Placeholder("player_rank", user.getIslandRank().name()));
        placeholderList.add(new Placeholder("player_join", user.getJoinTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
        return this;
    }

    public List<Placeholder> build() {
        return placeholderList;
    }
}
