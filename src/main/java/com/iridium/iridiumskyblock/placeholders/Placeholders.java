package com.iridium.iridiumskyblock.placeholders;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Placeholders {

    private static final com.iridium.iridiumskyblock.configs.Placeholders placeholdersConfig = IridiumSkyblock.getInstance().getPlaceholders();
    private static final IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();

    public static Map<String, Placeholder> placeholders = ImmutableMap.<String, Placeholder>builder()
            .putAll(getIslandPlaceholders("island", player ->
                    IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland())
            )
            .putAll(getIslandPlaceholders("current", player ->
                    IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player))
            )
            .putAll(getIslandTopPlaceholders())

            .put("player_rank", player -> {
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                return user.getIslandRank().getDisplayName();
            })
            .build();

    private static Map<String, Placeholder> getIslandPlaceholders(String startKey, IslandGetter islandGetter) {
        ImmutableMap.Builder<String, Placeholder> placeholderBuilder = ImmutableMap.<String, Placeholder>builder()
                .put(startKey + "_name", player ->
                        islandGetter.getIsland(player).map(Island::getName).orElse(placeholdersConfig.islandName)
                )
                .put(startKey + "_owner", player ->
                        islandGetter.getIsland(player).map(island -> island.getOwner().getName()).orElse(placeholdersConfig.islandOwner)
                )
                .put(startKey + "_rank", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getRank())).orElse(placeholdersConfig.islandRank)
                )
                .put(startKey + "_level", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getLevel())).orElse(placeholdersConfig.islandLevel)
                )
                .put(startKey + "_value", player ->
                        islandGetter.getIsland(player).map(Island::getFormattedValue).orElse(placeholdersConfig.islandValue)
                )
                .put(startKey + "_visitable", player ->
                        islandGetter.getIsland(player).map(island -> island.isVisitable() ? IridiumSkyblock.getInstance().getMessages().islandVisitable : IridiumSkyblock.getInstance().getMessages().islandNotVisitable).orElse(placeholdersConfig.islandValue)
                )
                .put(startKey + "_members", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getMembers().size())).orElse(placeholdersConfig.islandMembers)
                )
                .put(startKey + "_member_names", player ->
                        islandGetter.getIsland(player).map(island -> island.getMembers().stream().map(User::getName).collect(Collectors.joining(", "))).orElse(placeholdersConfig.islandMemberNames)
                )
                .put(startKey + "_visitors", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).stream().filter(user -> !island.equals(user.getIsland().orElse(null))).count())).orElse(placeholdersConfig.islandVisitors)
                )
                .put(startKey + "_visitor_names", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).stream().filter(user -> !island.equals(user.getIsland().orElse(null))).map(User::getName).collect(Collectors.joining(", "))).orElse(placeholdersConfig.islandVisitorNames)
                )
                .put(startKey + "_players", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).size())).orElse(placeholdersConfig.islandPlayers)
                )
                .put(startKey + "_player_names", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island).stream().map(User::getName).collect(Collectors.joining(", "))).orElse(placeholdersConfig.islandPlayerNames)
                )
                .put(startKey + "_experience", player ->
                        islandGetter.getIsland(player).map(Island::getFormattedExperience).orElse(placeholdersConfig.islandExperience)
                )
                .put(startKey + "_experience_required", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRequiredToLevelUp())).orElse(placeholdersConfig.islandExperienceRequired)
                )
                .put(startKey + "_experience_remaining", player ->
                        islandGetter.getIsland(player).map(island -> IridiumSkyblock.getInstance().getNumberFormatter().format(island.getExperienceRemainingToLevelUp())).orElse(placeholdersConfig.islandExperienceRemaining)
                )
                .put(startKey + "_overworld_biome", player ->
                        islandGetter.getIsland(player)
                                .map(island -> island.getCenter(IridiumSkyblock.getInstance().getIslandManager().getWorld()).getBlock())
                                .map(Block::getBiome)
                                .map(biome -> WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " ")))
                                .orElse(placeholdersConfig.islandBiome)
                )
                .put(startKey + "_nether_biome", player ->
                        islandGetter.getIsland(player)
                                .map(island -> island.getCenter(IridiumSkyblock.getInstance().getIslandManager().getNetherWorld()).getBlock())
                                .map(Block::getBiome)
                                .map(biome -> WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " ")))
                                .orElse(placeholdersConfig.islandBiome)
                )
                .put(startKey + "_end_biome", player ->
                        islandGetter.getIsland(player)
                                .map(island -> island.getCenter(IridiumSkyblock.getInstance().getIslandManager().getEndWorld()).getBlock())
                                .map(Block::getBiome)
                                .map(biome -> WordUtils.capitalizeFully(biome.name().toLowerCase().replace("_", " ")))
                                .orElse(placeholdersConfig.islandBiome)
                )

                // Island Bank Placeholders
                .put(startKey + "_bank_experience", player ->
                        islandGetter.getIsland(player).map(Island::getFormattedBankExperience).orElse(placeholdersConfig.islandBankExperience)
                )
                .put(startKey + "_bank_crystals", player ->
                        islandGetter.getIsland(player).map(Island::getFormattedCrystals).orElse(placeholdersConfig.islandBankCrystals)
                )
                .put(startKey + "_bank_money", player ->
                        islandGetter.getIsland(player).map(Island::getFormattedMoney).orElse(placeholdersConfig.islandBankMoney)
                )

                // Island Upgrade Placeholders
                .put(startKey + "_upgrade_member_amount", player -> islandGetter.getIsland(player).map(island ->
                        IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().memberUpgrade.upgrades.get(islandManager.getIslandUpgrade(island, "member").getLevel()).amount)
                ).orElse(placeholdersConfig.islandUpgradeMemberAmount))

                .put(startKey + "_upgrade_size_dimensions", player -> islandGetter.getIsland(player).map(island ->
                        IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().sizeUpgrade.upgrades.get(islandManager.getIslandUpgrade(island, "size").getLevel()).size)
                ).orElse(placeholdersConfig.islandUpgradeSizeDimensions))

                .put(startKey + "_upgrade_warp_amount", player -> islandGetter.getIsland(player).map(island ->
                        IridiumSkyblock.getInstance().getNumberFormatter().format(IridiumSkyblock.getInstance().getUpgrades().warpsUpgrade.upgrades.get(islandManager.getIslandUpgrade(island, "warp").getLevel()).amount)
                ).orElse(placeholdersConfig.islandUpgradeWarpAmount));

        //Boosters
        for (String booster : IridiumSkyblock.getInstance().getBoosterList().keySet()) {
            placeholderBuilder.put(startKey + "_booster_" + booster + "_remaining_minutes", player -> islandGetter.getIsland(player).map(island -> {
                        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island, booster);
                        long minutes = LocalDateTime.now().until(islandBooster.getTime(), ChronoUnit.MINUTES);
                        return String.valueOf(Math.max(minutes, 0));
                    }
            ).orElse(placeholdersConfig.islandBoosterRemainingMinutes));
            placeholderBuilder.put(startKey + "_booster_" + booster + "_remaining_seconds", player -> islandGetter.getIsland(player).map(island -> {
                        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island, booster);
                        long minutes = LocalDateTime.now().until(islandBooster.getTime(), ChronoUnit.MINUTES);
                        long seconds = LocalDateTime.now().until(islandBooster.getTime(), ChronoUnit.SECONDS) - minutes * 60;
                        return String.valueOf(Math.max(seconds, 0));
                    }
            ).orElse(placeholdersConfig.islandBoosterRemainingSeconds));
        }

        // Upgrade levels
        for (String upgrade : IridiumSkyblock.getInstance().getUpgradesList().keySet()) {
            placeholderBuilder.put(startKey + "_upgrade_" + upgrade + "_level", player -> islandGetter.getIsland(player).map(island ->
                    IridiumSkyblock.getInstance().getNumberFormatter().format(islandManager.getIslandUpgrade(island, upgrade).getLevel())
            ).orElse(placeholdersConfig.islandUpgradeLevel));
        }
        return placeholderBuilder.build();
    }

    private static Map<String, Placeholder> getIslandTopPlaceholders() {
        HashMap<String, Placeholder> hashmap = new HashMap<>();
        for (int i = 1; i <= 20; i++) {
            int finalI = i;
            hashmap.putAll(getIslandPlaceholders("island_top_" + i, player -> Optional.of(IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE).get(finalI - 1))));
        }
        return hashmap;
    }

    public interface Placeholder {

        String placeholderProcess(Player player);

    }

    public interface IslandGetter {

        Optional<Island> getIsland(Player player);

    }

}
