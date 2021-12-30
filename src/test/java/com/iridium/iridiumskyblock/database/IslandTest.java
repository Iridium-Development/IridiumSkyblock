package com.iridium.iridiumskyblock.database;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IslandTest {

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
    public void getCenter() {
        IridiumSkyblock.getInstance().getConfiguration().distance = 1;
        assertEquals(new Location(null, 0, 0, 0), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 0, 0, -1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 1, 0, -1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 1, 0, 0), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 1, 0, 1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 0, 0, 1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, -1, 0, 1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, -1, 0, 0), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, -1, 0, -1), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, -1, 0, -2), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 0, 0, -2), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 1, 0, -2), new IslandBuilder().build().getCenter(null));
        assertEquals(new Location(null, 2, 0, -2), new IslandBuilder().build().getCenter(null));
    }

}