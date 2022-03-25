package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.entity.Player;

public class InvalidMissionType extends AbstarctOpenInventory{
    public InvalidMissionType() {
    }

    boolean openPlayerInventory(Player player) {
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidMissionType.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        return false;
    }
}