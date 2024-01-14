package com.iridium.iridiumskyblock.managers;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.UserBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IslandManagerTest {

    private ServerMock serverMock;

    @BeforeEach
    public void setup() {
        this.serverMock = MockBukkit.mock();
        MockBukkit.load(IridiumSkyblock.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void createTeam_ShouldCreateAnIsland() {
        PlayerMock playerMock = new UserBuilder(serverMock).build();

        IridiumSkyblock.getInstance().getIslandManager().createTeam(playerMock, null);

        assertEquals(1, IridiumSkyblock.getInstance().getIslandManager().getTeams().size());
        assertTrue(IridiumSkyblock.getInstance().getUserManager().getUser(playerMock).getIsland().isPresent());
    }
}