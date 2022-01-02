package com.iridium.iridiumskyblock.utils;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomAccessListTest {

    @Test
    public void testRandomAccessListEmpty() {
        RandomAccessList<Integer> randomAccessList = new RandomAccessList<>(ImmutableMap.<Integer, Integer>builder().build());
        assertTrue(randomAccessList.nextElement().isEmpty());
    }

    @Test
    public void testRandomAccessListOneValue() {
        RandomAccessList<Integer> randomAccessList = new RandomAccessList<>(ImmutableMap.<Integer, Integer>builder()
                .put(1, 1)
                .build());
        assertEquals(1, randomAccessList.nextElement().orElse(null));
    }

    @Test
    public void testRandomAccessListMultipleValues() {
        RandomAccessList<Integer> randomAccessList = new RandomAccessList<>(ImmutableMap.<Integer, Integer>builder()
                .put(1, 1)
                .put(2, 1)
                .build());

        for (int i = 0; i < 1000; i++) {
            int number = randomAccessList.nextElement().orElse(0);
            assertTrue(number == 1 || number == 2);
        }
    }
}