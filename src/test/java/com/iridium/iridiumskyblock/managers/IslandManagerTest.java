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
        for (int i = 1; i <= 10; i++) {
            assertEquals(new IslandBuilder().build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(i).orElse(null));
        }
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