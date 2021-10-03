package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.database.Island;

public interface SettingValueChange {
    void run(Island island, String newValue);
}
