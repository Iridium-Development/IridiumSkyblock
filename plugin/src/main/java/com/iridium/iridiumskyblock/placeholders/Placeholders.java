package com.iridium.iridiumskyblock.placeholders;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class Placeholders {

    public static Map<String, Placeholder> placeholders = ImmutableMap.<String, Placeholder>builder()
            // Island Placeholders
            .put("island_name", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getName).orElse("N/A");
            })
            .put("island_owner", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> island.getOwner().getName()).orElse("N/A");
            })
            .put("island_rank", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getRank())).orElse("N/A");
            })
            .put("island_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getLevel())).orElse("N/A");
            })
            .put("island_value", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getValue())).orElse("N/A");
            })
            .put("island_experience", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getExperience())).orElse("N/A");
            })
            .put("island_experience_required", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getExperienceRequiredToLevelUp())).orElse("N/A");
            })
            .put("island_experience_remaining", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> String.valueOf(island.getExperienceRemainingToLevelUp())).orElse("N/A");
            })

            // Visiting Island Placeholders
            .put("current_island_name", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(Island::getName).orElse("N/A");
            })
            .put("current_island_owner", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> island.getOwner().getName()).orElse("N/A");
            })
            .put("current_island_rank", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getRank())).orElse("N/A");
            })
            .put("current_island_level", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getLevel())).orElse("N/A");
            })
            .put("current_island_value", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getValue())).orElse("N/A");
            })
            .put("current_island_experience", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getExperience())).orElse("N/A");
            })
            .put("current_island_experience_required", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getExperienceRequiredToLevelUp())).orElse("N/A");
            })
            .put("current_island_experience_remaining", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                return islandOptional.map(island -> String.valueOf(island.getExperienceRemainingToLevelUp())).orElse("N/A");
            })

            //Player Placeholders
            .put("player_rank", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIslandRank().name();
            })
            .build();

    public interface Placeholder {
        String placeholderProcess(Player player);
    }
}
