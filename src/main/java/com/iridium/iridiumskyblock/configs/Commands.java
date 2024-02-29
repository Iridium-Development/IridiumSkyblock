package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumskyblock.commands.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;

public class Commands extends com.iridium.iridiumteams.configs.Commands<Island, User> {
    public Commands() {
        super("iridiumskyblock", "Island", "is");
    }

    public VisitCommand visitCommand = new VisitCommand();
    public BorderCommand borderCommand = new BorderCommand();
    public RegenCommand regenCommand = new RegenCommand();
    public BiomeCommand biomeCommand = new BiomeCommand();
    public ClearDataCommand clearDataCommand = new ClearDataCommand();
}
