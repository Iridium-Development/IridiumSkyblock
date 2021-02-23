package com.iridium.iridiumskyblock.nms;

import com.iridium.iridiumskyblock.Color;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public interface NMS {
    void setBlockFast(World world, int x, int y, int z, int blockId, byte data, boolean applyPhysics);

    void sendChunk(List<Player> players, Chunk chunk);

    void sendWorldBorder(Player player, Color color, double size, Location centerLocation);

    void sendSubTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut);

    void sendTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut);
}
