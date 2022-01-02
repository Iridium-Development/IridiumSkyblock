package com.iridium.iridiumskyblock.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomAccessListTest {

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
    public void testRandomAccessListEmpty() {
        RandomAccessList<XMaterial> randomAccessList = new RandomAccessList<>(ImmutableMap.<XMaterial, Integer>builder().build());
        assertTrue(randomAccessList.nextElement().isEmpty());
    }

    @Test
    public void testRandomAccessListOneValue() {
        RandomAccessList<XMaterial> randomAccessList = new RandomAccessList<>(ImmutableMap.<XMaterial, Integer>builder()
                .put(XMaterial.STONE, 1)
                .build());
        assertEquals(XMaterial.STONE, randomAccessList.nextElement().orElse(null));
    }

    @Test
    public void testRandomAccessListMultipleValues() {
        RandomAccessList<XMaterial> randomAccessList = new RandomAccessList<>(ImmutableMap.<XMaterial, Integer>builder()
                .put(XMaterial.STONE, 1)
                .put(XMaterial.GRASS, 1)
                .build());

        for (int i = 0; i < 1000; i++) {
            XMaterial material = randomAccessList.nextElement().orElse(null);
            assertTrue(material == XMaterial.STONE || material == XMaterial.GRASS);
        }
    }
}