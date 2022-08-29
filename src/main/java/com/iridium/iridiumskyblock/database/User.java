package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumteams.database.IridiumUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Optional<Island> getIsland(){
        return IridiumSkyblock.getInstance().getTeamManager().getTeamViaID(getTeamID());
    }

    public Optional<Island> getCurrentIsland() {
        Player player = getPlayer();
        if(currentIsland.isPresent() && currentIsland.get().isInIsland(player.getLocation())){
            return currentIsland;
        }
        setCurrentIsland(IridiumSkyblock.getInstance().getTeamManager().getTeamViaLocation(player.getLocation()));
        return currentIsland;
    }
}
