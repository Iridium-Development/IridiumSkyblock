package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumteams.database.IridiumUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class User extends IridiumUser<Island> {

    private Optional<Island> currentIsland = Optional.empty();

    public User(UUID uuid, String name) {
        setUuid(uuid);
        setName(name);
        setJoinTime(LocalDateTime.now());
    }

    public Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(getTeamID());
    }

    public Optional<Island> getCurrentIsland() {
        Player player = getPlayer();
        if (player == null) return Optional.empty();
        if (currentIsland.isPresent() && currentIsland.get().isInIsland(player.getLocation())) {
            return currentIsland;
        }
        setCurrentIsland(IridiumSkyblock.getInstance().getTeamManager().getTeamViaLocation(player.getLocation()));
        return currentIsland;
    }

    public Optional<Island> getCurrentIsland(Location location) {
        Player player = getPlayer();
        if (player == null) return Optional.empty();
        if (currentIsland.isPresent() && currentIsland.get().isInIsland(location)) {
            return currentIsland;
        }
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getTeamViaLocation(location);

        islandOptional.ifPresent(island -> {
            if(island.isInIsland(player.getLocation())){
                setCurrentIsland(islandOptional);
            }
        });

        return islandOptional;
    }
}
