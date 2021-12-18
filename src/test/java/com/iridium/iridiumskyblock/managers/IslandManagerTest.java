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
        Island island = new IslandBuilder("Island Name").build();
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("Island Name").orElse(null));
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("ISLAND NAME").orElse(null));
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island name").orElse(null));

        assertNull(IridiumSkyblock.getInstance().getIslandManager().getIslandByName("fake_island").orElse(null));
        assertNull(IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island1").orElse(null));
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