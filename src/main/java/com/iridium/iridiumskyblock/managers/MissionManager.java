package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.database.IslandReward;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MissionManager {

    /**
     * Determines the missions to be checked
     *
     * @param island      The island
     * @param missionType The mission type e.g. BREAK
     * @param identifier  The mission identifier e.g. COBBLESTONE
     * @param increment   The amount we are incrementing by
     */
    public void handleMissionUpdates(@NotNull Island island, @NotNull String missionType, @NotNull String identifier, int increment) {
        incrementMission(island, missionType + ":" + identifier, increment);

        incrementMission(island, missionType + ":ANY", increment);

        for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getMissions().customMaterialLists.entrySet()) {
            if (itemList.getValue().contains(identifier)) {
                incrementMission(island, missionType + ":" + itemList.getKey(), increment);
            }
        }
    }

    /**
     * Increments a mission's data based on requirements.
     *
     * @param island      The island
     * @param missionData The mission data e.g. BREAK:COBBLESTONE
     * @param increment   The amount we are incrementing by
     */
    public synchronized void incrementMission(@NotNull Island island, @NotNull String missionData, int increment) {
        String[] missionConditions = missionData.toUpperCase().split(":");

        for (Map.Entry<String, Mission> entry : IridiumSkyblock.getInstance().getMissionsList().entrySet()) {
            boolean completedBefore = true;
            List<String> missions = entry.getValue().getMissions();
            for (int i = 0; i < entry.getValue().getMissions().size(); i++) {
                String missionRequirement = missions.get(i).toUpperCase();
                String[] conditions = missionRequirement.split(":");
                // If the conditions are the same length (+1 because missionConditions doesn't include amount)
                if (missionConditions.length + 1 != conditions.length) continue;

                // Check if this is a mission we want to increment
                boolean matches = matchesMission(missionConditions, conditions);
                if (!matches) continue;

                IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, entry.getValue(), entry.getKey(), i);
                String number = conditions[missionData.split(":").length];

                // Validate the required number for this condition
                if (number.matches("^[0-9]+$")) {
                    int amount = Integer.parseInt(number);
                    if (islandMission.getProgress() >= amount) break;
                    completedBefore = false;
                    islandMission.setProgress(Math.min(islandMission.getProgress() + increment, amount));
                } else {
                    IridiumSkyblock.getInstance().getLogger().warning("Unknown format " + missionRequirement);
                    IridiumSkyblock.getInstance().getLogger().warning(number + " Is not a number");
                }
            }

            // Check if this mission is now completed
            if (!completedBefore && hasCompletedMission(island, entry.getValue(), entry.getKey())) {
                island.getMembers().stream().map(user -> Bukkit.getPlayer(user.getUuid())).filter(Objects::nonNull).forEach(player -> {
                    entry.getValue().getMessage().stream().map(string -> StringUtils.color(string.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))).forEach(player::sendMessage);
                    entry.getValue().getCompleteSound().play(player);
                });
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().addEntry(new IslandReward(island, entry.getValue().getReward()));
            }
        }
    }

    /**
     * Checks if the given conditions are a part of the provided mission conditions.
     *
     * @param missionConditions The mission conditions
     * @param conditions        The conditions from missions.yml
     * @return Whether the conditions are a part of the mission conditions
     */
    private boolean matchesMission(String[] missionConditions, String[] conditions) {
        for (int j = 0; j < missionConditions.length; j++) {
            if (!(conditions[j].equals(missionConditions[j]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether or not the Island has completed the provided mission.
     *
     * @param island  The Island which should be checked
     * @param mission The mission which should be checked
     * @param key     The key of the mission
     * @return Whether or not this mission has been completed
     */
    private boolean hasCompletedMission(@NotNull Island island, @NotNull Mission mission, @NotNull String key) {
        List<String> missions = mission.getMissions();
        for (int i = 0; i < mission.getMissions().size(); i++) {
            String missionRequirement = missions.get(i).toUpperCase();
            IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, i);
            String[] conditions = missionRequirement.split(":");
            String number = conditions[conditions.length - 1];

            // Validate the required number for this condition
            if (number.matches("^[0-9]+$")) {
                int requiredAmount = Integer.parseInt(number);
                if (islandMission.getProgress() < requiredAmount) {
                    return false;
                }
            } else {
                IridiumSkyblock.getInstance().getLogger().warning("Unknown format " + missionRequirement);
                IridiumSkyblock.getInstance().getLogger().warning(number + " Is not a number");
            }
        }
        return true;
    }

}
