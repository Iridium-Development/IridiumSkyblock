package com.iridium.iridiumskyblock.configs;

public class Messages extends com.iridium.iridiumteams.configs.Messages {

    public Messages() {
        super("Island", "is", "IridiumSkyblock", "&9");
    }

    public String VoidTeleport = "%prefix% &7You have fallen off your island. Teleporting home...";
    public String netherIslandsDisabled = "%prefix% &7Nether islands have been disabled.";
    public String islandBorderChanged = "%prefix% &7%player% has changed your Island border to %color%.";
    public String borderColorDisabled = "%prefix% &7That border color has been disabled.";
    public String notAColor = "%prefix% &7That is not a valid color.";
    public String cannotManageBorder = "%prefix% &7You cannot change the Island Border.";
}
