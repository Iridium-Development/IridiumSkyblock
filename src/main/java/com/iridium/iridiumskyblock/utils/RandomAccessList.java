package com.iridium.iridiumskyblock.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * List which holds elements with certain possibilities and can randomly generate a random one via
 * {@link RandomAccessList#nextElement()}.
 *
 * @param <E> The type of elements in this list
 */
public class RandomAccessList<E> {

    private final List<E> underlyingList = new ArrayList<>();

    /**
     * Adds an element with a certain possibility to this list.
     * The possibility depends on the possibilities of the other elements, it is relative.
     *
     * @param e The element which should be added
     * @param possibility The possibility of this element being selected.
     */
    public void add(E e, int possibility) {
        for (int i = 0; i < possibility; i++) {
            underlyingList.add(e);
        }
    }

    /**
     * Adds a Map of elements to the list where the key is an element and the value its possibility.
     * The possibility depends on the possibilities of the other elements, it is relative.
     *
     * @param possibilities Map of the elements with their possibilities
     */
    public void addAll(Map<E, Integer> possibilities) {
        for (E e : possibilities.keySet()) {
            add(e, possibilities.get(e));
        }
    }

    /**
     * Generates a random element from this list, considering the specified possibilities.
     *
     * @return A random element
     */
    public Optional<E> nextElement() throws IndexOutOfBoundsException {
        if (underlyingList.isEmpty()) return Optional.empty();
        int randomIndex = ThreadLocalRandom.current().nextInt(underlyingList.size());
        return Optional.of(underlyingList.get(randomIndex));
    }

}
