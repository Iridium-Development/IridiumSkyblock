package com.iridium.iridiumskyblock.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.ProtocolLibUtils.IridiumPacketAdapter;

public class ProtocolLibPacketListener {

    public void registerListeners() {
        try {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            protocolManager.addPacketListener(new IridiumPacketAdapter(IridiumSkyblock.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.LOGIN));
            protocolManager.addPacketListener(new IridiumPacketAdapter(IridiumSkyblock.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.RESPAWN));
        } catch (NoClassDefFoundError e) {
            IridiumSkyblock.getInstance().getLogger().severe(e.getMessage());
        }
    }
}
