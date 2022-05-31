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
        assertEquals(new Location(null, 0, 0, 0), new IslandBuilder(1).build().getCenter(null));
        assertEquals(new Location(null, 0, 0, -1), new IslandBuilder(2).build().getCenter(null));
        assertEquals(new Location(null, 1, 0, -1), new IslandBuilder(3).build().getCenter(null));
        assertEquals(new Location(null, 1, 0, 0), new IslandBuilder(4).build().getCenter(null));
        assertEquals(new Location(null, 1, 0, 1), new IslandBuilder(5).build().getCenter(null));
        assertEquals(new Location(null, 0, 0, 1), new IslandBuilder(6).build().getCenter(null));
        assertEquals(new Location(null, -1, 0, 1), new IslandBuilder(7).build().getCenter(null));
        assertEquals(new Location(null, -1, 0, 0), new IslandBuilder(8).build().getCenter(null));
        assertEquals(new Location(null, -1, 0, -1), new IslandBuilder(9).build().getCenter(null));
        assertEquals(new Location(null, -1, 0, -2), new IslandBuilder(10).build().getCenter(null));
        assertEquals(new Location(null, 0, 0, -2), new IslandBuilder(11).build().getCenter(null));
        assertEquals(new Location(null, 1, 0, -2), new IslandBuilder(12).build().getCenter(null));
        assertEquals(new Location(null, 2, 0, -2), new IslandBuilder(13).build().getCenter(null));
    }

}