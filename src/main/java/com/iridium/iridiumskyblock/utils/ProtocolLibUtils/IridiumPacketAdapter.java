package com.iridium.iridiumskyblock.utils.ProtocolLibUtils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.generators.IridiumChunkGenerator;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class IridiumPacketAdapter extends PacketAdapter {

    public IridiumPacketAdapter(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
        super(plugin, listenerPriority, types);
    }

    @Override
    public void onPacketSending(PacketEvent packetEvent) {

        if(IridiumSkyblock.getInstance().getConfiguration().fixHorizon
                && (IridiumSkyblock.getInstance().getChunkGenerator() instanceof IridiumChunkGenerator)) {
            if (((IridiumChunkGenerator) IridiumSkyblock.getInstance().getChunkGenerator()).isLowerHorizon()) {
                sendLowerHorizon(packetEvent);
            }
        }
    }

    private void sendLowerHorizon(PacketEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        World world = event.getPlayer().getWorld();
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(world) && world.getEnvironment() != World.Environment.NORMAL) return;

        IridiumSkyblock.getInstance().getLogger().info("[DEBUG] " + IridiumSkyblock.getInstance().getMcVersion());
        editPacketForHorizon(event, IridiumSkyblock.getInstance().getMcVersion() < 16);
    }

    private void editPacketForHorizon(PacketEvent packetEvent, boolean fallback) {

        PacketContainer packet = packetEvent.getPacket(); //packetEvent.getPacket().deepClone();
        PacketType packetType = packet.getType();

        if (!fallback) {

            // login
            // to get all | StructureModifier<Object> modifier = packet.getModifier();
            // BOOLEANS | StructureModifier<Boolean> booleans = packet.getBooleans();
            // b - [0] hardcore (false)
            // g -
            // h -
            // i -
            // ENTITY | StructureModifier<Entity> entity = packet.getEntityModifier(packetEvent);
            // a -
            // d -
            // e -
            // f -
            // PLAYER INFO DATA | StructureModifier<List<PlayerInfoData>> playerData = packet.getPlayerInfoDataLists();
            // c -

            // MISSING
            // j -

            // Packet Capture
            //    a=331    // ??? entity type?
            //    b=false  // hardcore mode
            //    c=[ResourceKey[minecraft:dimension / minecraft:overworld], ResourceKey[minecraft:dimension / minecraft:the_nether], ResourceKey[minecraft:dimension / minecraft:the_end], ResourceKey[minecraft:dimension / minecraft:iridiumskyblock], ResourceKey[minecraft:dimension / minecraft:plugins/iridiumskyblock/regenworlds/iridiumskyblock_regen], ResourceKey[minecraft:dimension / minecraft:iridiumskyblock_nether], ResourceKey[minecraft:dimension / minecraft:plugins/iridiumskyblock/regenworlds/iridiumskyblock_nether_regen], ResourceKey[minecraft:dimension / minecraft:plugins/iridiumskyblock/regenworlds/iridiumskyblock_the_end_regen], ResourceKey[minecraft:dimension / minecraft:iridiumskyblock_the_end]]
            //    d=20     // probably the amount of health the player has
            //    e=10
            //    f=10
            //    g=false
            //    h=true
            //    i=false
            //    j=CommonPlayerSpawnInfo[dimensionType=ResourceKey[minecraft:dimension_type / minecraft:overworld], dimension=ResourceKey[minecraft:dimension / minecraft:iridiumskyblock], seed=8029251825143012118, gameType=CREATIVE, previousGameType=SURVIVAL, isDebug=false, isFlat=false, lastDeathLocation=Optional[ResourceKey[minecraft:dimension / minecraft:iridiumskyblock] BlockPosition{x=1, y=82, z=2}], portalCooldown=0]

            int flatWorldIndex = 0;

            if(packetType == PacketType.Play.Server.LOGIN) flatWorldIndex = 4;
            if(packetType == PacketType.Play.Server.RESPAWN) flatWorldIndex = 1;

            Field commonPlayerSpawnInfo = packet.getModifier().getField(9);

            int getCommonPlayerSpawnInfoIndex = 0;
            if(packetType == PacketType.Play.Server.LOGIN) getCommonPlayerSpawnInfoIndex = 9;
            if(packetType == PacketType.Play.Server.RESPAWN) getCommonPlayerSpawnInfoIndex = 0;

            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] =+=+=+=+=+=+=+=+=+=+=+=+=");
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PACKET TYPE: " + packetType);
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PACKET STRUCTURES: " + packet.getStructures());
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PACKET FIELDS: " + packet.getStructures().getFields());
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PACKET WORLD TYPE: " + packet.getWorldTypeModifier().getField(WorldType.FLAT.ordinal()));
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] =+=+=+=+=+=+=+=+=+=+=+=+=");
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PACKET OBJECT: " + commonPlayerSpawnInfo);
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] PRE-MUTATION VALUES: " + commonPlayerSpawnInfo.toString());
            IridiumSkyblock.getInstance().getLogger().info("[DEBUG] =+=+=+=+=+=+=+=+=+=+=+=+=");
            
            //booleans.write(flatWorldIndex, true);
            packet.getWorldTypeModifier().getField(WorldType.FLAT.ordinal());

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
