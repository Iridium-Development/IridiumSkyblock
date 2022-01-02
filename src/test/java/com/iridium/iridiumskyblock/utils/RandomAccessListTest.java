package com.iridium.iridiumskyblock.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandomAccessListTest {

    @Test
    public void testRandomAccessList() {
        RandomAccessList<Integer> randomAccessList = new RandomAccessList<>();
        randomAccessList.add(1, 1);
        randomAccessList.add(2, 2);
        randomAccessList.add(3, 3);

        assertEquals(6, randomAccessList.getSize());

        for (int i = 0; i < 1000; i++) {
            assertNotNull(randomAccessList.nextElement().orElse(null));
        }
    }

}
