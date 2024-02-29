package com.iridium.iridiumskyblock.configs;

public class Messages extends com.iridium.iridiumteams.configs.Messages {

    public Messages() {
        super("Island", "is", "IridiumSkyblock", "&9");

        teamCreated = "%prefix% &7Island Creation Completed!";
    }

    public String voidTeleport = "%prefix% &7You have fallen off your island. Teleporting home...";
    public String itemsString = "%amount% %item_name%";
    public String voidLostItems = "%prefix% &7You've lost %items%!";
    public String netherIslandsDisabled = "%prefix% &7Nether islands have been disabled.";
    public String netherLocked = "%prefix% &7Reach Island level %level% to unlock the Nether.";
    public String endIslandsDisabled = "%prefix% &7End islands have been disabled.";
    public String endLocked = "%prefix% &7Reach Island level %level% to unlock the End.";
    public String islandBorderChanged = "%prefix% &7%player% has changed your Island border to %color%.";
    public String borderColorDisabled = "%prefix% &7That border color has been disabled.";
    public String notAColor = "%prefix% &7That is not a valid color.";
    public String cannotManageBorder = "%prefix% &7You cannot change the Island Border.";
    public String regeneratingIsland = "%prefix% &7Regenerating Island...";
    public String cannotRegenIsland = "%prefix% &7You cannot regenerate your Island.";
    public String unknownSchematic = "%prefix% &7No schematic with that name exists.";
    public String paidForRegen = "%prefix% &7%player% successfully regenerated the island (%schematic%&7) for %vault_cost%.";
    public String noSafeLocation = "%prefix% &7Could not find a safe location to teleport to.";
    public String cannotHurtPlayers = "%prefix% &7You cannot hurt players on your Island.";
    public String creatingIsland = "%prefix% &7Creating Island, please wait...";
    public String noBiomeCategory = "%prefix% &7No biome category with that name.";
    public String noBiome = "%prefix% &7No biome with that name.";
    public String changedBiome = "%prefix% &7%player% successfully changed your Island biome to %biome%.";
    public String specifyData = "%prefix% &7Due to the nature of this destructive command, you must specify the data to be deleted (or specify \"all\").";
    public String confirmDataDeletion = "%prefix% &7You are about to delete the following data: %table% | [for: %island%] in 5 seconds. &cAre you sure you want to do this? (DATA IS &4&lUNRECOVERABLE&r&c)";
    public String dataDeletion = "%prefix% &cData has been deleted successfully. &7You may need to restart your server for it to take effect.";
}
