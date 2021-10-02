package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Placeholders {

    // Island Placeholders.
    public String islandName = "N/A";
    public String islandOwner = "N/A";
    public String islandRank = "0";
    public String islandLevel = "0";
    public String islandValue = "0";
    public String islandMembers = "0";
    public String islandMemberNames = "No members";
    public String islandVisitors = "0";
    public String islandVisitorNames = "No visitors";
    public String islandPlayers = "0";
    public String islandPlayerNames = "No players";
    public String islandExperience = "0";
    public String islandExperienceRequired = "0";
    public String islandExperienceRemaining = "0";
    public String islandBankExperience = "0";
    public String islandBankCrystals = "0";
    public String islandBankMoney = "0";
    public String islandBiome = "N/A";

    // Island Upgrade Placeholders
    public String islandUpgradeBlocklimitLevel = "0";
    public String islandUpgradeMemberLevel = "0";
    public String islandUpgradeSizeLevel = "0";
    public String islandUpgradeGeneratorLevel = "0";
    public String islandUpgradeWarpLevel = "0";
    public String islandUpgradeMemberAmount = "0";
    public String islandUpgradeSizeDimensions = "0";
    public String islandUpgradeWarpAmount = "0";

    // Other Placeholders
    public String unknownPlayer = "N/A";
    public String crystalCost = "0";
    public String vaultCost = "0";
    
}
