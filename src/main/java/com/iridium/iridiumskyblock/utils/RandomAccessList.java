package com.iridium.iridiumskyblock.utils;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * List which holds elements with certain possibilities and can randomly generate a random one via
 * {@link RandomAccessList#nextElement()}.
 *
 * @param <E> The type of elements in this list
 */
@AllArgsConstructor
public class RandomAccessList<E> {

    private final Map<E, Integer> underlyingList;

    /**
     * Generates a random element from this list, considering the specified possibilities.
     *
     * @return A random element
     */
    public Optional<E> nextElement() {
        if (underlyingList.isEmpty()) return Optional.empty();
        int listSize = underlyingList.values().stream().mapToInt(Integer::intValue).sum();
        int randomIndex = ThreadLocalRandom.current().nextInt(listSize);
        int counter = 0;
        for (E e : underlyingList.keySet()) {
            if (randomIndex >= counter && randomIndex < counter + underlyingList.get(e)) {
                return Optional.of(e);
            }
            counter += underlyingList.get(e);
        }
        // The code should never reach here
        return Optional.empty();
    }

}
