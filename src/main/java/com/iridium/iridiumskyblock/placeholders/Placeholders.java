package com.iridium.iridiumskyblock.placeholders;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class Placeholders {

    public static Map<String, Placeholder> placeholders = ImmutableMap.<String, Placeholder>builder()
            // Island Placeholders
            .put("island_name", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getName).orElse(defaultValue());
            })
            .put("island_owner", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> island.getOwner().getName()).orElse(defaultValue());
            })
            .put("island_rank", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getRank())).orElse(defaultValue());
            })
            .put("island_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getLevel())).orElse(defaultValue());
            })
            .put("island_value", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getFormattedValue).orElse(defaultValue());
            })
            .put("island_members", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getMembers().size())).orElse(defaultValue());
            })
            .put("island_experience", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getFormattedExperience).orElse(defaultValue());
            })
            .put("island_experience_required", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRequiredToLevelUp())).orElse(defaultValue());
            })
            .put("island_experience_remaining", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRemainingToLevelUp())).orElse(defaultValue());
            })
            .put("island_bank_experience", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getFormattedBankExperience).orElse(defaultValue());
            })
            .put("island_bank_crystals", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getFormattedCrystals).orElse(defaultValue());
            })
            .put("island_bank_money", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIsland().map(Island::getFormattedMoney).orElse(defaultValue());
            })

            // Island Upgrade Placeholders
            .put("island_upgrade_blocklimit_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "blocklimit");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(islandUpgrade.getLevel());
            })
            .put("island_upgrade_member_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "member");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(islandUpgrade.getLevel());
            })
            .put("island_upgrade_size_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "size");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(islandUpgrade.getLevel());
            })
            .put("island_upgrade_generator_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "generator");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(islandUpgrade.getLevel());
            })
            .put("island_upgrade_warp_level", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "warp");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(islandUpgrade.getLevel());
            })
            .put("island_upgrade_member_amount", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "member");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().memberUpgrade.upgrades.get(islandUpgrade.getLevel()).amount);
            })
            .put("island_upgrade_size_dimensions", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "size");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().sizeUpgrade.upgrades.get(islandUpgrade.getLevel()).size);
            })
            .put("island_upgrade_warp_amount", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                if (!user.getIsland().isPresent()) return defaultValue();
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(user.getIsland().get(), "warp");
                return IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().warpsUpgrade.upgrades.get(islandUpgrade.getLevel()).amount);
            })

            // Visiting Island Placeholders
            .put("current_island_name", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getName).orElse(defaultValue());
            })
            .put("current_island_owner", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(island -> island.getOwner().getName()).orElse(defaultValue());
            })
            .put("current_island_rank", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getRank())).orElse(defaultValue());
            })
            .put("current_island_level", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getLevel())).orElse(defaultValue());
            })
            .put("current_island_value", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getFormattedValue).orElse(defaultValue());
            })
            .put("current_island_experience", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getFormattedExperience).orElse(defaultValue());
            })
            .put("current_island_experience_required", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRequiredToLevelUp())).orElse(defaultValue());
            })
            .put("current_island_experience_remaining", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRemainingToLevelUp())).orElse(defaultValue());
            })
            .put("current_island_bank_experience", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getFormattedBankExperience).orElse(defaultValue());
            })
            .put("current_island_bank_crystals", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getFormattedCrystals).orElse(defaultValue());
            })
            .put("current_island_bank_money", player -> {
                Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
                return islandOptional.map(Island::getFormattedMoney).orElse(defaultValue());
            })

            // Player Placeholders
            .put("player_rank", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIslandRank().getDisplayName();
            })
            .build();

    private static String defaultValue() {
        return IridiumSkyblock.getInstance().getConfiguration().placeholderDefaultValue;
    }
    
    public interface Placeholder {

        String placeholderProcess(Player player);

    }

}
