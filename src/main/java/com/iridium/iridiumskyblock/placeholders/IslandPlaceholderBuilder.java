package com.iridium.iridiumskyblock.placeholders;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.PlaceholderBuilder;
import com.iridium.iridiumteams.Rank;
import com.iridium.iridiumteams.bank.BankItem;
import org.bukkit.entity.EntityType;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IslandPlaceholderBuilder implements PlaceholderBuilder<Island> {
    @Override
    public List<Placeholder> getPlaceholders(Island island) {
        List<User> users = IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island);
        List<String> onlineUsers = users.stream()
                .filter(u -> u.getPlayer() != null)
                .map(User::getName)
                .collect(Collectors.toList());
        List<String> offlineUsers = users.stream()
                .filter(u -> u.getPlayer() == null)
                .map(User::getName)
                .collect(Collectors.toList());

        List<Placeholder> placeholderList = new ArrayList<>(Arrays.asList(
                new Placeholder("island_name", island.getName()),
                new Placeholder("island_owner", IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island).stream()
                        .filter(user -> user.getUserRank() == Rank.OWNER.getId())
                        .findFirst()
                        .map(User::getName)
                        .orElse("N/A")),
                new Placeholder("island_create", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))),
                new Placeholder("island_description", island.getDescription()),
                new Placeholder("island_value", String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamValue(island))),
                new Placeholder("island_level", String.valueOf(island.getLevel())),
                new Placeholder("island_experience", String.valueOf(island.getExperience())),
                new Placeholder("island_value_rank", String.valueOf(IridiumSkyblock.getInstance().getTop().valueTeamSort.getRank(island, IridiumSkyblock.getInstance()))),
                new Placeholder("island_experience_rank", String.valueOf(IridiumSkyblock.getInstance().getTop().experienceTeamSort.getRank(island, IridiumSkyblock.getInstance()))),
                new Placeholder("island_members_online", String.join(", ", onlineUsers)),
                new Placeholder("island_members_online_count", String.valueOf(onlineUsers.size())),
                new Placeholder("island_members_offline", String.join(", ", offlineUsers)),
                new Placeholder("island_members_offline_count", String.valueOf(offlineUsers.size())),
                new Placeholder("island_members_count", String.valueOf(users.size()))
        ));
        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            placeholderList.add(new Placeholder("island_bank_" + bankItem.getName(), String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamBank(island, bankItem.getName()).getNumber())));
        }
        for (XMaterial xMaterial : XMaterial.values()) {
            placeholderList.add(new Placeholder("island_" + xMaterial.name().toUpperCase() + "_AMOUNT", String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamBlock(island, xMaterial).getAmount())));
        }
        for (EntityType entityType : EntityType.values()) {
            placeholderList.add(new Placeholder("island_" + entityType.name().toUpperCase() + "_AMOUNT", String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamSpawners(island, entityType).getAmount())));
        }
        return placeholderList;
    }

    public List<Placeholder> getDefaultPlaceholders() {
        List<Placeholder> placeholderList = new ArrayList<>(Arrays.asList(
                new Placeholder("island_name", "N/A"),
                new Placeholder("island_owner", "N/A"),
                new Placeholder("island_description", "N/A"),
                new Placeholder("island_create", "N/A"),
                new Placeholder("island_value", "N/A"),
                new Placeholder("island_level", "N/A"),
                new Placeholder("island_experience", "N/A"),
                new Placeholder("island_value_rank", "N/A"),
                new Placeholder("island_experience_rank", "N/A"),
                new Placeholder("island_members_online", "N/A"),
                new Placeholder("island_members_online_count", "N/A"),
                new Placeholder("island_members_offline", "N/A"),
                new Placeholder("island_members_offline_count", "N/A"),
                new Placeholder("island_members_count", "N/A")
        ));
        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            placeholderList.add(new Placeholder("island_bank_" + bankItem.getName(), "N/A"));
        }
        for (XMaterial xMaterial : XMaterial.values()) {
            placeholderList.add(new Placeholder("island_" + xMaterial.name().toUpperCase() + "_AMOUNT", "N/A"));
        }
        for (EntityType entityType : EntityType.values()) {
            placeholderList.add(new Placeholder("island_" + entityType.name().toUpperCase() + "_AMOUNT", "N/A"));
        }
        return placeholderList;
    }

    @Override
    public List<Placeholder> getPlaceholders(Optional<Island> optional) {
        return optional.isPresent() ? getPlaceholders(optional.get()) : getDefaultPlaceholders();
    }
}
