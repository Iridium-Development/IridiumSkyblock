package com.iridium.iridiumskyblock.nms;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Interface for working with the net.minecraft.server package.
 * Version-specific, so it has to be implemented for every version we support.
 */
public interface NMS {

  /**
   * Sets blocks faster than with Spigots implementation.
   * See https://www.spigotmc.org/threads/methods-for-changing-massive-amount-of-blocks-up-to-14m-blocks-s.395868/
   * for more information.
   *
   * @param world        The world where the block should be placed
   * @param x            The x position of the block
   * @param y            The y position of the block
   * @param z            The z position of the block
   * @param blockId      The ID of this block, used for backwards-compatibility with 1.8 - 1.12
   * @param data         The data of this block
   * @param applyPhysics Whether or not to apply physics
   */
  void setBlockFast(World world, int x, int y, int z, int blockId, byte data, boolean applyPhysics);

  /**
   * Sends the provided chunk to all the specified players.
   * Used for updating chunks.
   *
   * @param players The player which should see the updated chunk
   * @param chunk   The chunk which should be updated
   */
  void sendChunk(List<Player> players, Chunk chunk);

  /**
   * Sends a colored world border to the specified Player with the provided size and center location.
   * The size is half of the length of one side of the border.
   *
   * @param player         The Player which should see the border
   * @param color          The color of the border
   * @param size           The size of this border, see the description above for more information
   * @param centerLocation The center of the border
   */
  void sendWorldBorder(Player player, Color color, double size, Location centerLocation);

  /**
   * Sends a subtitle with the provided properties to the Player.
   *
   * @param player      The Player which should see the subtitle
   * @param message     The message of the subtitle
   * @param fadeIn      The amount of time this subtitle should fade in in ticks
   * @param displayTime The amount of time this subtitle should stay fully visible in ticks
   * @param fadeOut     The amount of time this subtitle should fade out in ticks
   */
  void sendSubTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut);

  /**
   * Sends a title with the provided properties to the Player.
   *
   * @param player      The Player which should see the title
   * @param message     The message of the title
   * @param fadeIn      The amount of time this title should fade in in ticks
   * @param displayTime The amount of time this title should stay fully visible in ticks
   * @param fadeOut     The amount of time this title should fade out in ticks
   */
  void sendTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut);

}
