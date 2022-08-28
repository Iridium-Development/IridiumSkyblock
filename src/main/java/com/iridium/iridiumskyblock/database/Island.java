package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumteams.database.Team;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Island extends Team {
    public Island(String name) {
        setName(name);
        setDescription(IridiumSkyblock.getInstance().getConfiguration().defaultDescription);
    }

    public Island(int id) {
        setId(id);
    }

    @Override
    public double getValue() {
        return 0;
    }
}
