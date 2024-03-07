package com.iridium.iridiumskyblock.placeholders;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.PlaceholderBuilder;
import com.iridium.iridiumteams.Rank;
import com.iridium.iridiumteams.TemporaryCache;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.database.TeamEnhancement;
import com.iridium.iridiumteams.enhancements.Enhancement;
import com.iridium.iridiumteams.enhancements.EnhancementType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IslandPlaceholderBuilder implements PlaceholderBuilder<Island> {

    private final TemporaryCache<Island, List<Placeholder>> cache = new TemporaryCache<>();
    private final List<Placeholder> defaultPlaceholders = initializeDefaultPlaceholders();

    @Override
    public List<Placeholder> getPlaceholders(Island island) {
        return cache.get(island, Duration.ofSeconds(1), () -> {
            List<User> users = IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island);

            List<String> onlineUsers = new ArrayList<>(Collections.emptyList());
            List<String> offlineUsers = new ArrayList<>(Collections.emptyList());

            for(User user : users) {
                if(user.getPlayer() != null) onlineUsers.add(user.getName());
                offlineUsers.add(user.getName());
            }

            List<Placeholder> placeholderList = new ArrayList<>(Arrays.asList(
                    new Placeholder("island_name", () -> island.getName()),
                    new Placeholder("island_owner", () -> IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island).stream()
                            .filter(user -> user.getUserRank() == Rank.OWNER.getId())
                            .findFirst()
                            .map(User::getName)
                            .orElse("N/A")),
                    new Placeholder("island_create", () -> island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))),
                    new Placeholder("island_description", () -> island.getDescription()),
                    new Placeholder("island_value", () -> String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamValue(island))),
                    new Placeholder("island_level", () -> String.valueOf(island.getLevel())),
                    new Placeholder("island_experience", () -> String.valueOf(island.getExperience())),
                    new Placeholder("island_value_rank", () -> String.valueOf(IridiumSkyblock.getInstance().getTop().valueTeamSort.getRank(island, IridiumSkyblock.getInstance()))),
                    new Placeholder("island_experience_rank", () -> String.valueOf(IridiumSkyblock.getInstance().getTop().experienceTeamSort.getRank(island, IridiumSkyblock.getInstance()))),
                    new Placeholder("island_members_online", () -> String.join(", ", onlineUsers)),
                    new Placeholder("island_members_online_count", () -> String.valueOf(onlineUsers.size())),
                    new Placeholder("island_members_offline", () -> String.join(", ", offlineUsers)),
                    new Placeholder("island_members_offline_count", () -> String.valueOf(offlineUsers.size())),
                    new Placeholder("island_members_count", () -> String.valueOf(users.size()))
            ));

            List<Player> visitingPlayers = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getPlayer)
                    .filter(player -> island.isInIsland(player.getLocation()))
                    .collect(Collectors.toList());

            visitingPlayers.removeIf(player -> onlineUsers.contains(player.getName()));

            placeholderList.add(new Placeholder("island_visitors", () -> visitingPlayers.stream().map(Player::getName).collect(Collectors.joining(", "))));
            placeholderList.add(new Placeholder("island_visitors_amount", () -> String.valueOf(visitingPlayers.size())));

            placeholderList.add(new Placeholder("island_visitors", () -> visitingPlayers.stream().map(Player::getName).collect(Collectors.joining(", "))));
            placeholderList.add(new Placeholder("island_visitors_amount", () -> String.valueOf(visitingPlayers.size())));

            for(Map.Entry<String, Enhancement<?>> enhancement : IridiumSkyblock.getInstance().getEnhancementList().entrySet()) {
                TeamEnhancement teamEnhancement = IridiumSkyblock.getInstance().getIslandManager().getTeamEnhancement(island, enhancement.getKey());

                if(enhancement.getValue().enabled && enhancement.getValue().type == EnhancementType.BOOSTER) {
                    placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_active", () -> String.valueOf(teamEnhancement.isActive())));
                    placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_level", () -> String.valueOf(teamEnhancement.getLevel())));
                    placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_hours", () -> String.valueOf(Math.max((int) (teamEnhancement.getRemainingTime() % 60), 0))));
                    placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_minutes", () -> String.valueOf(Math.max((int) ((teamEnhancement.getRemainingTime() % 3600) / 60), 0))));
                    placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_seconds", () -> String.valueOf(Math.max((int) (teamEnhancement.getRemainingTime() / 3600), 0))));
                }

                if(enhancement.getValue().enabled && enhancement.getValue().type == EnhancementType.UPGRADE) {
                    placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_active", () -> String.valueOf(teamEnhancement.isActive())));
                    placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_level", () -> String.valueOf(teamEnhancement.getLevel())));
                    placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_hours", () -> String.valueOf(Math.max((int) (teamEnhancement.getRemainingTime() % 60), 0))));
                    placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_minutes", () -> String.valueOf(Math.max((int) ((teamEnhancement.getRemainingTime() % 3600) / 60), 0))));
                    placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_seconds", () -> String.valueOf(Math.max((int) (teamEnhancement.getRemainingTime() / 3600), 0))));
                }
            }

            for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
                placeholderList.add(new Placeholder("island_bank_" + bankItem.getName().toLowerCase(), () -> String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamBank(island, bankItem.getName()).getNumber())));
            }
            for (XMaterial xMaterial : XMaterial.values()) {
                placeholderList.add(new Placeholder("island_" + xMaterial.name().toLowerCase() + "_amount", () -> String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamBlock(island, xMaterial).getAmount())));
            }
            for (EntityType entityType : EntityType.values()) {
                placeholderList.add(new Placeholder("island_" + entityType.name().toLowerCase() + "_amount", () -> String.valueOf(IridiumSkyblock.getInstance().getTeamManager().getTeamSpawners(island, entityType).getAmount())));
            }
            return placeholderList;
        });
    }

    private List<Placeholder> initializeDefaultPlaceholders() {

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
                new Placeholder("island_members_count", "N/A"),
                new Placeholder("island_visitors", "N/A"),
                new Placeholder("island_visitors_amount", "N/A")
        ));

        for(Map.Entry<String, Enhancement<?>> enhancement : IridiumSkyblock.getInstance().getEnhancementList().entrySet()) {
            if(enhancement.getValue().type == EnhancementType.BOOSTER) {
                placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_active", "N/A"));
                placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_level", "N/A"));
                placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_hours", "N/A"));
                placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_minutes", "N/A"));
                placeholderList.add(new Placeholder("island_booster_" + enhancement.getKey() + "_time_seconds", "N/A"));
            }

            if(enhancement.getValue().type == EnhancementType.UPGRADE) {
                placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_active", "N/A"));
                placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_level", "N/A"));
                placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_hours", "N/A"));
                placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_minutes", "N/A"));
                placeholderList.add(new Placeholder("island_upgrade_" + enhancement.getKey() + "_time_seconds", "N/A"));
            }
        }

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            placeholderList.add(new Placeholder("island_bank_" + bankItem.getName().toLowerCase(), "N/A"));
        }
        for (XMaterial xMaterial : XMaterial.values()) {
            placeholderList.add(new Placeholder("island_" + xMaterial.name().toLowerCase() + "_amount", "N/A"));
        }
        for (EntityType entityType : EntityType.values()) {
            placeholderList.add(new Placeholder("island_" + entityType.name().toLowerCase() + "_amount", "N/A"));
        }
        return placeholderList;
    }

    @Override
    public List<Placeholder> getPlaceholders(Optional<Island> optional) {
        return optional.isPresent() ? getPlaceholders(optional.get()) : defaultPlaceholders;
    }
}
