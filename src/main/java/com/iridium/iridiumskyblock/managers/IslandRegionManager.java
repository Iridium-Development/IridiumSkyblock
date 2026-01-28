package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages WorldGuard regions for IridiumSkyblock islands.
 * Handles region creation, updates, deletion, and access control across all worlds.
 */
public class IslandRegionManager {

    private final WorldGuardPlugin worldGuardPlugin;

    /**
     * Creates a new IslandRegionManager instance.
     * Should only be instantiated if WorldGuard is present.
     */
    public IslandRegionManager() {
        this.worldGuardPlugin = WorldGuardPlugin.inst();
    }

    /**
     * Creates WorldGuard regions for the given island across all enabled worlds.
     * Regions include the island area plus a configurable buffer for wilderness gaps.
     *
     * @param island The island to create regions for
     */
    public void createIslandRegions(Island island) {
        if (!IridiumSkyblock.getInstance().getConfiguration().useWorldGuardRegions) {
            return;
        }

        IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.forEach((environment, enabled) -> {
            if (!enabled) return;

            World world = IridiumSkyblock.getInstance().getTeamManager().getWorld(environment);
            if (world == null) return;

            RegionManager wgRegionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (wgRegionManager == null) return;

            // Calculate region bounds: island area + buffer
            Location minLoc = island.getPosition1(world);
            Location maxLoc = island.getPosition2(world);

            int buffer = IridiumSkyblock.getInstance().getConfiguration().regionBufferSize;
            BlockVector3 min = BlockVector3.at(minLoc.getBlockX() - buffer, world.getMinHeight(), minLoc.getBlockZ() - buffer);
            BlockVector3 max = BlockVector3.at(maxLoc.getBlockX() + buffer, world.getMaxHeight(), maxLoc.getBlockZ() + buffer);

            // Create the region
            String regionName = getRegionName(island);
            ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionName, min, max);

            // Set flags for island protection - EXIT flag prevents walking/falling out
            region.setFlag(Flags.EXIT, StateFlag.State.DENY);
            // EXIT_VIA_TELEPORT allows command teleports to bypass the exit restriction
            region.setFlag(Flags.EXIT_VIA_TELEPORT, StateFlag.State.ALLOW);

            // Set region owners (island members) using UUIDs
            List<User> members = IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island);
            for (User member : members) {
                region.getOwners().addPlayer(member.getUuid());
            }

            // Add region to world
            wgRegionManager.addRegion(region);
        });

        // Save regions to disk
        saveRegions();
    }

    /**
     * Updates existing regions for an island (e.g., after island upgrade/size change).
     *
     * @param island The island whose regions to update
     */
    public void updateIslandRegions(Island island) {
        // Delete existing regions and recreate them with new bounds
        deleteIslandRegions(island);
        createIslandRegions(island);
    }

    /**
     * Deletes WorldGuard regions for the given island across all worlds.
     *
     * @param island The island whose regions to delete
     */
    public void deleteIslandRegions(Island island) {
        if (!IridiumSkyblock.getInstance().getConfiguration().useWorldGuardRegions) {
            return;
        }

        IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.forEach((environment, enabled) -> {
            if (!enabled) return;

            World world = IridiumSkyblock.getInstance().getTeamManager().getWorld(environment);
            if (world == null) return;

            RegionManager wgRegionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (wgRegionManager == null) return;

            String regionName = getRegionName(island);
            wgRegionManager.removeRegion(regionName);
        });

        // Save regions to disk
        saveRegions();
    }

    /**
     * Updates the members/owners of an island's regions.
     * Should be called when members join or leave an island.
     *
     * @param island The island whose region members to update
     */
    public void updateIslandRegionMembers(Island island) {
        if (!IridiumSkyblock.getInstance().getConfiguration().useWorldGuardRegions) {
            return;
        }

        IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.forEach((environment, enabled) -> {
            if (!enabled) return;

            World world = IridiumSkyblock.getInstance().getTeamManager().getWorld(environment);
            if (world == null) return;

            RegionManager wgRegionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (wgRegionManager == null) return;

            String regionName = getRegionName(island);
            ProtectedRegion region = wgRegionManager.getRegion(regionName);
            if (region == null) return;

            // Clear existing owners and re-add current members
            region.getOwners().clear();
            List<User> members = IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island);
            for (User member : members) {
                region.getOwners().addPlayer(member.getUuid());
            }
        });

        saveRegions();
    }

    /**
     * Checks if a player is attempting to exit an island region by walking.
     * WorldGuard's EXIT flag handles this automatically, but this method can be used
     * for custom messaging or additional logic.
     *
     * @param player The player attempting to move
     * @param from   Current location
     * @param to     Destination location
     * @return true if the move would exit an island region, false otherwise
     */
    public boolean isExitingIslandRegion(Player player, Location from, Location to) {
        if (!IridiumSkyblock.getInstance().getConfiguration().useWorldGuardRegions) {
            return false;
        }

        if (from.getWorld() == null || to.getWorld() == null) return false;
        if (!from.getWorld().equals(to.getWorld())) return false;

        World world = from.getWorld();
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(world)) {
            return false;
        }

        RegionManager wgRegionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (wgRegionManager == null) return false;

        ApplicableRegionSet fromRegions = wgRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(from));
        ApplicableRegionSet toRegions = wgRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(to));

        // Check if leaving an island region
        for (ProtectedRegion region : fromRegions) {
            if (region.getId().startsWith("island_")) {
                // Check if destination is outside this region
                BlockVector3 toVector = BlockVector3.at(to.getBlockX(), to.getBlockY(), to.getBlockZ());
                if (!region.contains(toVector)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the region name for an island.
     *
     * @param island The island
     * @return The region name
     */
    private String getRegionName(Island island) {
        return "island_" + island.getId();
    }

    /**
     * Saves all region changes to disk.
     */
    private void saveRegions() {
        try {
            IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.forEach((environment, enabled) -> {
                if (!enabled) return;

                World world = IridiumSkyblock.getInstance().getTeamManager().getWorld(environment);
                if (world == null) return;

                RegionManager wgRegionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
                if (wgRegionManager == null) return;

                try {
                    wgRegionManager.save();
                } catch (Exception e) {
                    IridiumSkyblock.getInstance().getLogger().warning("Failed to save WorldGuard regions for world: " + world.getName());
                }
            });
        } catch (Exception e) {
            IridiumSkyblock.getInstance().getLogger().warning("Failed to save WorldGuard regions: " + e.getMessage());
        }
    }
}
