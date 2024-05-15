package com.iridium.iridiumskyblock.utils.ProtocolLibUtils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class IridiumPacketAdapter extends PacketAdapter {

    public IridiumPacketAdapter(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        if (IridiumSkyblock.getInstance().getConfiguration().fixHorizon
            && IridiumSkyblock.getInstance().getConfiguration().generatorType.isLowerHorizon()) sendLowerHorizon(packetEvent);
    }

    private void sendLowerHorizon(PacketEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        World world = event.getPlayer().getWorld();
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(world) && world.getEnvironment() != World.Environment.NORMAL) return;

        IridiumSkyblock.getInstance().getLogger().info("[DEBUG] Version: " + Bukkit.getBukkitVersion() + " (" + IridiumSkyblock.getInstance().getMcVersion() + ")");
        editPacketForHorizon(event, IridiumSkyblock.getInstance().getMcVersion() < 16);
    }

    private void editPacketForHorizon(PacketEvent packetEvent, boolean fallback) {

        PacketContainer packet = packetEvent.getPacket().deepClone();
        PacketType packetType = packet.getType();

        if (!fallback) {

            StructureModifier<Boolean> booleans = packet.getBooleans();

            if (packetType == PacketType.Play.Server.LOGIN) booleans.write(4, true);
            else if (packetType == PacketType.Play.Server.RESPAWN) booleans.write(1, true);

        } else {

            if (packetType == WrapperPlayServerLogin.TYPE) {
                WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
                wrapperPlayServerLogin.setLevelType(WorldType.FLAT);
                wrapperPlayServerLogin.sendPacket(packetEvent.getPlayer());
            } else if (packetType == WrapperPlayServerRespawn.TYPE) {
                WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
                wrapperPlayServerRespawn.setLevelType(WorldType.FLAT);
                wrapperPlayServerRespawn.sendPacket(packetEvent.getPlayer());
            }
        }
    }
}
