package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Placeholders {

    // Island Placeholders.
    public String island_name = "N/A";
    public String island_owner = "N/A";
    public String island_rank = "0";
    public String island_level = "0";
    public String island_value = "0";
    public String island_members = "1";
    public String island_experience = "0";
    public String island_experience_required = "0";
    public String island_experience_remaining = "0";
    public String island_bank_experience = "0";
    public String island_bank_crystals = "0";
    public String island_bank_money = "0";

    // Island Upgrade Placeholders
    public String island_upgrade_blocklimit_level = "0";
    public String island_upgrade_member_level = "0";
    public String island_upgrade_size_level = "0";
    public String island_upgrade_generator_level = "0";
    public String island_upgrade_warp_level = "0";
    public String island_upgrade_member_amount = "0";
    public String island_upgrade_size_dimensions = "0";
    public String island_upgrade_warp_amount = "0";

    // Visiting island placeholders
    public String current_island_name = "N/A";
    public String current_island_owner = "N/A";
    public String current_island_rank = "N/A";
    public String current_island_level = "0";
    public String current_island_value = "0";
    public String current_island_experience = "0";
    public String current_island_experience_required = "0";
    public String current_island_experience_remaining = "0";
    public String current_island_bank_experience = "0";
    public String current_island_bank_crystals = "0";
    public String current_island_bank_money = "0";

    // Other Placeholders
    public String unknownPlayer = "N/A";
    public String crystalCost = "0";
    public String vaultCost = "0";
}
