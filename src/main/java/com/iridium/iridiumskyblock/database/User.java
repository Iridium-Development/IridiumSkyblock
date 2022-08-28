package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumteams.database.IridiumUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class User extends IridiumUser<Island> {
    public User(UUID uuid, String name) {
        setUuid(uuid);
        setName(name);
        setJoinTime(LocalDateTime.now());
    }

    public Optional<Island> getIsland(){
        return null;
    }
}
