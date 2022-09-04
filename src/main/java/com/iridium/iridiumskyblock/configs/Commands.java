package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumskyblock.commands.BorderCommand;
import com.iridium.iridiumskyblock.commands.RegenCommand;
import com.iridium.iridiumskyblock.commands.VisitCommand;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;

public class Commands extends com.iridium.iridiumteams.configs.Commands<Island, User> {
    public Commands() {
        super("iridiumskyblock.", "Island", "is");
    }

    public VisitCommand visitCommand = new VisitCommand();
    public BorderCommand borderCommand = new BorderCommand();
    public RegenCommand regenCommand = new RegenCommand();
}
