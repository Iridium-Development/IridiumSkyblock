package com.iridium.iridiumskyblock.managers;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandBuilder;
import com.iridium.iridiumskyblock.UserBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IslandManagerTest {

    private ServerMock serverMock;

    @BeforeEach
    public void setup() {
        this.serverMock = MockBukkit.mock();
        MockBukkit.load(IridiumSkyblock.class);
    }

    @AfterEach
    public void tearDown() {
        Bukkit.getScheduler().cancelTasks(IridiumSkyblock.getInstance());
        MockBukkit.unmock();
    }

    @Test
    public void getIslandById() {
        assertEquals(new IslandBuilder(1).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(1).orElse(null));
        assertEquals(new IslandBuilder(2).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(2).orElse(null));
        assertEquals(new IslandBuilder(3).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(3).orElse(null));
    }

    @Test
    public void getIslandByName() {
        Island island1 = new IslandBuilder("Island 1").build();
        Island island2 = new IslandBuilder("Island 2").build();
        assertEquals(island1, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("Island 1").orElse(null));
        assertEquals(island1, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("ISLAND 1").orElse(null));
        assertEquals(island1, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island 1").orElse(null));

        assertEquals(island2, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("Island 2").orElse(null));
        assertEquals(island2, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("ISLAND 2").orElse(null));
        assertEquals(island2, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island 2").orElse(null));
    }

    @Test
    public void getIslandMembers() {
        Island island = new IslandBuilder().build();
        User user1 = new UserBuilder(serverMock).withIsland(island).buildUser();
        User user2 = new UserBuilder(serverMock).withIsland(island).buildUser();
        assertEquals(2, IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island).size());
        assertTrue(IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island).contains(user1));
        assertTrue(IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island).contains(user2));
    }
}