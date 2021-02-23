package com.iridium.iridiumskyblock.nms;

import com.iridium.iridiumskyblock.Color;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class v1_16_R3 implements NMS {

    @Override
    public void setBlockFast(org.bukkit.World world, int x, int y, int z, int blockId, byte data, boolean applyPhysics) {
        World nmsWorld = ((CraftWorld) world).getHandle();
        Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);
        IBlockData ibd = Block.getByCombinedId(blockId + (data << 12));

        ChunkSection cs = nmsChunk.getSections()[y >> 4];
        if (cs == null) {
            cs = new ChunkSection(y >> 4 << 4);
            nmsChunk.getSections()[y >> 4] = cs;
        }
        cs.setType(x & 15, y & 15, z & 15, ibd);
        nmsChunk.getWorld().getChunkProvider().getLightEngine().a(new BlockPosition(x, y, z));
    }

    @Override
    public void sendChunk(List<Player> players, org.bukkit.Chunk chunk) {
        PacketPlayOutMapChunk packetPlayOutMapChunk = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 65535);
        players.forEach(player ->
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutMapChunk)
        );
    }

    @Override
    public void sendWorldBorder(Player player, Color color, double size, Location centerLocation) {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.world = ((CraftWorld) centerLocation.getWorld()).getHandle();
        worldBorder.setCenter(centerLocation.getBlockX() + 0.5, centerLocation.getBlockZ() + 0.5);

        if (color == Color.OFF) {
            worldBorder.setSize(Integer.MAX_VALUE);
        } else {
            worldBorder.setSize(size);
        }

        worldBorder.setWarningDistance(0);
        worldBorder.setWarningTime(0);

        if (color == Color.RED) {
            worldBorder.transitionSizeBetween(size, size - 1.0D, 20000000L);
        } else if (color == Color.GREEN) {
            worldBorder.transitionSizeBetween(size - 0.1D, size, 20000000L);
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    @Override
    public void sendSubTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + message + "\"}"));
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iChatBaseComponent, fadeIn, displayTime, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }

    @Override
    public void sendTitle(Player player, String message, int fadeIn, int displayTime, int fadeOut) {
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + message + "\"}"));
        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iChatBaseComponent, fadeIn, displayTime, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutTitle);
    }
}
